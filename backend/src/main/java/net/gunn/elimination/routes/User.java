package net.gunn.elimination.routes;

import com.fasterxml.jackson.databind.ObjectMapper;
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

	ObjectMapper objectMapper = new ObjectMapper();

	public User(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping({"/", ""})
	@Transactional
	public Map me(@AuthenticationPrincipal EliminationAuthentication user) {
		EliminationUser me = userRepository.findBySubject(user.subject()).orElseThrow();

		Map res = objectMapper.convertValue(me, Map.class);
		res.put("eliminated", me.getEliminated());

		return res;
	}

	@PostMapping("/logout")
	public void logout(HttpServletRequest request) {
		request.getSession().invalidate();
	}
}
