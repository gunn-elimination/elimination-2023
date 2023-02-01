package net.gunn.elimination.routes.game;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.model.BulkKillfeed;
import net.gunn.elimination.model.Kill;
import net.gunn.elimination.repository.KillfeedRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping("/game")
@PreAuthorize("@eliminationManager.gameIsOngoing()")
@CrossOrigin(originPatterns = "*")
public class KillfeedController {
	private final KillfeedRepository killfeedRepository;

	public KillfeedController(KillfeedRepository killfeedRepository) {
		this.killfeedRepository = killfeedRepository;
	}

	@GetMapping(value = "/eliminations", produces = "application/json")
	@SentrySpan
	@ResponseBody
	@Transactional
	public Set killfeed() {
		BulkKillfeed bulkKillfeed = new BulkKillfeed(killfeedRepository.findAll(Sort.by(Sort.Direction.DESC, "timeStamp")));

		Set<Map> ret = new HashSet<>();

		for (Kill kill : bulkKillfeed.kills()) {
			ret.add(kill.makeMap());
		}

		return ret;
	}

}
