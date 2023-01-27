package net.gunn.elimination.routes.game;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.SSEController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;


@RestController
@RequestMapping("/game")
@PreAuthorize("@eliminationManager.gameIsOngoing() && hasRole('ROLE_PLAYER')")
@CrossOrigin(
	origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
	allowCredentials = "true",
	exposedHeaders = "SESSION"
)
public class GameController {
	public final EliminationManager eliminationManager;
	private final Optional<SSEController> sseController;
	private final UserRepository userRepository;

	private final EntityManagerFactory emf;


	public GameController(EliminationManager eliminationManager, Optional<SSEController> sseController, UserRepository userRepository, EntityManagerFactory emf) {
		this.eliminationManager = eliminationManager;
		this.sseController = sseController;
		this.userRepository = userRepository;
		this.emf = emf;
	}

	/**
	 * @apiNote Gets the current user's elimination code.
	 */
	@GetMapping(value = "/code", produces = "application/json")
	@SentrySpan
	@ResponseBody
	@Transactional
	public String code(@AuthenticationPrincipal EliminationAuthentication user_) {
		var user = userRepository.findBySubject(user_.subject()).orElseThrow();
		return user.getEliminationCode();
	}

	@GetMapping("/eliminate")
	@PostMapping("/eliminate")
	@SentrySpan
	@Transactional
	public void eliminate(HttpServletResponse response, @AuthenticationPrincipal EliminationAuthentication me_, @RequestParam("code") String code) throws IncorrectEliminationCodeException, EmptyGameException, IOException {
		var me = userRepository.findBySubject(me_.subject()).orElseThrow();
		var eliminated = eliminationManager.attemptElimination(me, code);

		sseController.ifPresent(sseController -> {
			sseController.signalKill(new Kill(me, eliminated));
			sseController.signalScoreboardChange();
		});

		response.sendRedirect("/");
	}

	@GetMapping("/target")
	@SentrySpan
	@ResponseBody
	@Transactional
	public Set target(@AuthenticationPrincipal EliminationAuthentication me_) {
		var me = userRepository.findBySubject(me_.subject()).orElseThrow();
		var target = me.getTarget();
		return Set.of(
			"email", target.getEmail(),
			"forename", target.getForename(),
			"surname", target.getSurname()
			);
	}

	@GetMapping("/eliminatedBy")
	@SentrySpan
	@ResponseBody
	@Transactional
	public Set eliminatedBy(@AuthenticationPrincipal EliminationAuthentication me) {
		var me_ = userRepository.findBySubject(me.subject()).orElseThrow();
		var eliminatedBy = me_.getEliminatedBy();

		if (eliminatedBy == null) {
			return Set.of();
		}
		return Set.of(
			"email", eliminatedBy.getEmail(),
			"forename", eliminatedBy.getForename(),
			"surname", eliminatedBy.getSurname()
		);
	}
}
