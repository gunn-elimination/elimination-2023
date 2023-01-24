package net.gunn.elimination.routes.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/game")
@CrossOrigin(originPatterns = "*")
public class ScoreboardController {
    private final UserRepository userRepository;
    private final Set<ScoreboardSubscription> scoreboardEmitters = ConcurrentHashMap.newKeySet();
    private final Set<SseEmitter> killEmitters = ConcurrentHashMap.newKeySet();

    private final EntityManagerFactory emf;

    public ScoreboardController(UserRepository userRepository, EntityManagerFactory emf) {
        this.userRepository = userRepository;
        this.emf = emf;
    }

    @GetMapping(value = "/scoreboard", produces = "application/json")
    @Transactional
    @ResponseBody
    @SentrySpan
    public Scoreboard scoreboard(@RequestParam(defaultValue = "20") int limit) {
        limit = Math.min(Math.max(limit, 0), 100);
        return new Scoreboard(userRepository.findTopByNumberOfEliminations().filter(user -> user.getEmail().endsWith("@pausd.us")).limit(limit).toList());
    }

    @GetMapping(value = "/scoreboard", produces = "text/event-stream")
    @ResponseBody
    @Transactional
    public SseEmitter scoreboardStream(@RequestParam(defaultValue = "20") int limit) throws IOException {
        var emitter = new SseEmitter(-1L);
        emitter.send(scoreboard(limit));
        // close tr

        var sub = new ScoreboardSubscription(emitter, limit);
        scoreboardEmitters.add(sub);
        emitter.onCompletion(() -> scoreboardEmitters.remove(sub));

        return emitter;
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
        for (var sub : new HashSet<>(scoreboardEmitters)) {
            try {
                sub.emitter.send(scoreboard(sub.limit));
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