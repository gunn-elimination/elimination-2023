package net.gunn.elimination.routes.game;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.Kill;
import net.gunn.elimination.repository.KillfeedRepository;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.SSEController;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;


@RestController
@RequestMapping("/game")
@PreAuthorize("@eliminationManager.gameIsOngoing() && hasRole('ROLE_PLAYER')")
@CrossOrigin(
	origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
	allowCredentials = "true",
	exposedHeaders = "SESSION"
)
public class KillfeedController {
	public final EliminationManager eliminationManager;
	private final UserRepository userRepository;
	private final KillfeedRepository killfeedRepository;

	private final EntityManagerFactory emf;


	public KillfeedController(EliminationManager eliminationManager, UserRepository userRepository, KillfeedRepository killfeedRepository, EntityManagerFactory emf) {
		this.eliminationManager = eliminationManager;
		this.userRepository = userRepository;
		this.killfeedRepository = killfeedRepository;
		this.emf = emf;
	}

	/**
	 * @apiNote Gets the current user's elimination code.
	 */
	@GetMapping(value = "/eliminations", produces = "application/json")
	@SentrySpan
	@ResponseBody
	@Transactional
	public List killfeed() {
		return killfeed0();
	}

	public List killfeed0() {
		List<Map> kills = new ArrayList<>();

		for (Kill kill : killfeedRepository.findAll()) {
			Map map = mapFromKill(kill);
			if (map != null) {
				kills.add(map);
			}
		}

		return kills;
	}

	public Map mapFromKill(Kill kill) {
		var eliminator = userRepository.findBySubject(kill.getEliminator());
		var eliminated = userRepository.findBySubject(kill.getEliminated());

		if (eliminator.isPresent() && eliminated.isPresent()) {
			return
				Map.of(
					"eliminator", eliminator.get().decompose(),
					"eliminated", eliminated.get().decompose(),
					"timestamp", kill.getTimeStamp()
				)
				;
		}
		return null; // potentially a future bug
	}

}