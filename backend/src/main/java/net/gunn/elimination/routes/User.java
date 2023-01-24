package net.gunn.elimination.routes;

import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.EliminationUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/me")
@CrossOrigin(
    origins = {"https://elimination-2023.vercel.app", "https://elimination.gunn.one", "http://localhost:3000"},
    allowCredentials = "true",
    exposedHeaders = "SESSION"
)
@PreAuthorize("!isAnonymous()")
public class User {
    @GetMapping({"/", ""})
    public EliminationUser me(@AuthenticationPrincipal EliminationAuthentication user) {
        return user.user();
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
