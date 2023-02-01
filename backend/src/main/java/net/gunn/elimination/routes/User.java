package net.gunn.elimination.routes;

import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
@RequestMapping("/me")
@CrossOrigin(
	origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
	allowCredentials = "true",
	exposedHeaders = "SESSION"
)
@PreAuthorize("!isAnonymous()")
public class User {
	private final UserRepository userRepository;

	public User(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping({"/", ""})
	@Transactional
	public Object me(@AuthenticationPrincipal EliminationAuthentication user) {
		var result = userRepository.findBySubject(user.subject()).orElseThrow();
		var eliminated = result.eliminated();

		var eliminatedMaps = new HashSet<Map>();
		for (var e : eliminated) {
			eliminatedMaps.add(
				Map.of(
					"forename", e.getForename(),
					"surname", e.getSurname(),
					"email", e.getEmail()
				)
			);
		}

		return Map.of(
			"forename", result.getForename(),
			"surname", result.getSurname(),
			"email", result.getEmail(),
			"eliminated", eliminatedMaps
		);
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		request.getSession().invalidate();
	}
}
