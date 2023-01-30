package net.gunn.elimination.routes.kills;

import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.auth.EliminationAuthentication;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@CrossOrigin(
	origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
	allowCredentials = "true",
	exposedHeaders = "SESSION"
)
public class KillController {
	private final KillService killService;

	private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

	public KillController(KillService killService) {
		this.killService = killService;
	}

	@PreAuthorize("@eliminationManager.gameIsOngoing()")
	@GetMapping(value = "/kills", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter kills() {
		var emitter = new SseEmitter();
		emitters.add(emitter);
		emitter.onCompletion(() -> emitters.remove(emitter));
		return emitter;
	}

	@PreAuthorize("@eliminationManager.gameIsOngoing() && hasRole('ROLE_PLAYER')")
	@PostMapping("/kill")
	public void kill(@RequestParam("code") String eliminationCode, @AuthenticationPrincipal EliminationAuthentication auth) throws IncorrectEliminationCodeException, EmptyGameException {
		killService.attemptElimination(auth.subject(), eliminationCode);
	}

	@RabbitListener(bindings =
	@QueueBinding(
		value = @Queue(""),
		exchange = @Exchange(name = "kills", type = "fanout")
	)
	)
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public void onKill(Kill kill) {
		emitters.forEach(emitter -> {
			try {
				emitter.send(kill);
			} catch (Exception e) {
				emitters.remove(emitter);
			}
		});
	}
}
