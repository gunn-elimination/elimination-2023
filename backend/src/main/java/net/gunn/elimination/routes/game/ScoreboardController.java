package net.gunn.elimination.routes.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/game")
@CrossOrigin(originPatterns = "*")
public class ScoreboardController {
	private final UserRepository userRepository;
	private final Set<ScoreboardSubscription> scoreboardEmitters = ConcurrentHashMap.newKeySet();
	private final Set<SseEmitter> killEmitters = ConcurrentHashMap.newKeySet();

	private final Map<String, EliminationUser> userCache = new ConcurrentHashMap<>();

	private final EntityManagerFactory emf;

	public ScoreboardController(UserRepository userRepository, EntityManagerFactory emf) {
		this.userRepository = userRepository;
		this.emf = emf;
	}

	@GetMapping(value = "/scoreboard", produces = "application/json")
	@Transactional(readOnly = true)
	@ResponseBody
	@SentrySpan
	public Scoreboard scoreboard(@RequestParam(defaultValue = "20") int limit) {
		return scoreboard0(limit);
	}

	private Scoreboard scoreboard0(@RequestParam(defaultValue = "20") int limit) {
		var users_ = userRepository.findTopByNumberOfEliminations(Pageable.ofSize(limit));
		var users = users_.stream().map(
			user -> userCache.computeIfAbsent(user, u -> userRepository.findBySubject(u).orElseThrow())
		).toList();
		return new Scoreboard(users);
	}

	@GetMapping(value = "/scoreboard", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter scoreboardStream(@RequestParam(defaultValue = "20") int limit) throws IOException {
		var em = emf.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();

		try {
			var emitter = new SseEmitter(-1L);
			emitter.send(scoreboard0(limit));
			// close tr

			var sub = new ScoreboardSubscription(emitter, limit);
			scoreboardEmitters.add(sub);
			emitter.onCompletion(() -> scoreboardEmitters.remove(sub));
			return emitter;
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

	}

	@GetMapping(value = "/eliminations", produces = "text/event-stream")
	public SseEmitter kills() {
		var emitter = new SseEmitter(-1L);

		killEmitters.add(emitter);
		emitter.onCompletion(() -> killEmitters.remove(emitter));
		pushScoreboard();

		return emitter;
	}

	@SentrySpan(description = "send scoreboard after update via sse")
	public void pushScoreboard() {
		var maxLimit = new HashSet<>(scoreboardEmitters).stream().max(Comparator.comparingInt(ScoreboardSubscription::limit)).get().limit;
		var sb = scoreboard0(maxLimit);
		for (var sub : new HashSet<>(scoreboardEmitters)) {
			try {
				var sublist = sb.users.subList(0, Math.min(sub.limit, sb.users.size()));
				sub.emitter.send(new Scoreboard(sublist));
			} catch (IOException e) {
				// ignore, spring should call onCompletion
			}
		}
	}

	@SentrySpan(description = "sends kill events to clients via see")
	void pushKill(Kill kill) {
		for (var emitter : new HashSet<>(killEmitters)) {
			try {
				emitter.send(kill);
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public record Scoreboard(@JsonProperty List<EliminationUser> users) {
	}

	public record ScoreboardSubscription(SseEmitter emitter, int limit) {
	}
}
