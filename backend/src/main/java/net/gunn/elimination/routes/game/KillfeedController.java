package net.gunn.elimination.routes.game;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.model.BulkKillfeed;
import net.gunn.elimination.model.Kill;
import net.gunn.elimination.repository.KillfeedRepository;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/game")
@PreAuthorize("@eliminationManager.gameIsOngoing()")
@CrossOrigin(originPatterns = "*")
public class KillfeedController {
	public final EliminationManager eliminationManager;
	private final KillfeedRepository killfeedRepository;


	public KillfeedController(EliminationManager eliminationManager, KillfeedRepository killfeedRepository) {
		this.eliminationManager = eliminationManager;
		this.killfeedRepository = killfeedRepository;
	}

	@GetMapping(value = "/eliminations", produces = "application/json")
	@SentrySpan
	@ResponseBody
	@Transactional
	public BulkKillfeed killfeed() {
		return new BulkKillfeed(killfeedRepository.findAll(Sort.by(Sort.Direction.DESC, "timestamp")));
	}

}
