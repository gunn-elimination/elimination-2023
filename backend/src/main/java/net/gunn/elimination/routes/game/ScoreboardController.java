package net.gunn.elimination.routes.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.auth.Roles;
import net.gunn.elimination.auth.Roles;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.model.Role;
import net.gunn.elimination.repository.UserRepository;
import org.hibernate.Hibernate;
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

	public ScoreboardController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping(value = "/scoreboard", produces = "application/json")
	@ResponseBody
	@SentrySpan
	public Scoreboard scoreboard(@RequestParam(defaultValue = "20") int limit) {
		return scoreboard0(limit);
	}

	private Scoreboard scoreboard0(@RequestParam(defaultValue = "20") int limit) {
		var users = userRepository.findTopByNumberOfEliminations(Pageable.ofSize(limit));
		Hibernate.initialize(users);
		return new Scoreboard(
			users
			, userRepository.countEliminationUsersByRolesContaining(Roles.PLAYER)
		);
	}

	public record Scoreboard(@JsonProperty List<EliminationUser> users, @JsonProperty int numParticipants) {
	}

	public record ScoreboardSubscription(SseEmitter emitter, int limit) {
	}
}
