package net.gunn.elimination.routes.scoreboard;

import io.micrometer.core.annotation.Timed;
import io.sentry.spring.jakarta.tracing.SentrySpan;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/game")
@CrossOrigin(originPatterns = "*")
@Timed
class ScoreboardController {
	@PersistenceContext
	private EntityManager entityManager;

	private final ScoreboardService scoreboardService;
	private final Set<ScoreboardSubscription> emitters = ConcurrentHashMap.newKeySet();


	public ScoreboardController(ScoreboardService scoreboardService) {
		this.scoreboardService = scoreboardService;
	}

	@GetMapping(value = "/scoreboard", produces = "application/json")
	@ResponseBody
	@SentrySpan
	public Scoreboard scoreboard(@RequestParam(defaultValue = "20") int limit) {
		return scoreboardService.getScoreboard(limit);
	}

	@GetMapping(value = "/scoreboard", produces = "text/event-stream")
	@ResponseBody
	@SentrySpan
	public SseEmitter scoreboardStream(@RequestParam(defaultValue = "20") int limit) throws IOException {
		var emitter = new SseEmitter(-1L);
		var subscription = new ScoreboardSubscription(emitter, limit);
		emitter.onCompletion(() -> emitters.remove(subscription));
		emitter.onTimeout(() -> emitters.remove(subscription));

		emitters.add(subscription);
		emitter.send(scoreboardService.getScoreboard(limit));
		return emitter;
	}

	@RabbitListener(bindings =
	@QueueBinding(
		value = @Queue(),
		exchange = @Exchange(name = "kills", type = "fanout")
	)
	)
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	private void sendScoreboardToAll() {
		var maxLimit = new HashSet<>(emitters).stream().max(Comparator.comparingInt(ScoreboardSubscription::limit)).get().limit();
		var scoreboard = scoreboardService.getScoreboard(maxLimit);

		for (var emitter : emitters) {
			try {
				var cappedScoreboard = new Scoreboard(scoreboard.users().subList(0, emitter.limit()));
				emitter.emitter().send(cappedScoreboard);
			} catch (IOException e) {
				// ignore
			}
		}
	}

}
