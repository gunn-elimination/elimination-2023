package net.gunn.elimination.routes.game;

import io.micrometer.core.annotation.Timed;
import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


@RestController
@RequestMapping("/game")
@PreAuthorize("@eliminationManager.gameIsOngoing() && hasRole('ROLE_PLAYER')")
@CrossOrigin(
	origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
	allowCredentials = "true",
	exposedHeaders = "SESSION"
)
@Timed
public class GameController {
	public final EliminationManager eliminationManager;
	private final UserRepository userRepository;

	public GameController(EliminationManager eliminationManager, UserRepository userRepository) {
		this.eliminationManager = eliminationManager;
		this.userRepository = userRepository;
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

		response.sendRedirect("/");
	}

	@GetMapping("/target")
	@SentrySpan
	@ResponseBody
	@Transactional
	public Map target(@AuthenticationPrincipal EliminationAuthentication me_, HttpServletResponse response) {
		var me = userRepository.findBySubject(me_.subject()).orElseThrow();
		var target = me.getTarget();

		Map userObj = null;
		if (target != null) {
			var targetEliminations = new HashSet<>();
			for (var eliminatee : target.eliminated()) {
				targetEliminations.add(
					Map.of(
						"forename", eliminatee.getForename(),
						"surname", eliminatee.getSurname(),
						"email", eliminatee.getEmail()
					)
				);
			}

			userObj = Map.of(
				"email", target.getEmail(),
				"forename", target.getForename(),
				"surname", target.getSurname(),
				"eliminated", targetEliminations
			);
		}

		var result = new HashMap<>();
		result.put("user", userObj);
		return result;
	}

	@GetMapping("/eliminatedBy")
	@SentrySpan
	@ResponseBody
	@Transactional
	public Map eliminatedBy(@AuthenticationPrincipal EliminationAuthentication me, HttpServletResponse response) {
		var me_ = userRepository.findBySubject(me.subject()).orElseThrow();
		var eliminatedBy = me_.getEliminatedBy();

		Map user = null;
		if (eliminatedBy != null) {
			user = Map.of(
				"email", eliminatedBy.getEmail(),
				"forename", eliminatedBy.getForename(),
				"surname", eliminatedBy.getSurname()
			);
		}

		var result = new HashMap<>();
		result.put("user", user);
		return result;
	}
}
