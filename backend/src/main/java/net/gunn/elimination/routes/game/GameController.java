package net.gunn.elimination.routes.game;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.EliminationUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


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
    private final ScoreboardController scoreboardController;


    public GameController(EliminationManager eliminationManager, ScoreboardController scoreboardController) {
        this.eliminationManager = eliminationManager;
        this.scoreboardController = scoreboardController;
    }

    /**
     * @apiNote Gets the current user's elimination code.
     */
    @GetMapping(value = "/code", produces = "application/json")
    @SentrySpan
    public String code(@AuthenticationPrincipal EliminationAuthentication user) {
        return user.user().getEliminationCode();
    }

    @GetMapping("/eliminate")
    @PostMapping("/eliminate")
    @SentrySpan
    public void eliminate(HttpServletResponse response,  @AuthenticationPrincipal EliminationAuthentication me, @RequestParam("code") String code) throws IncorrectEliminationCodeException, EmptyGameException, IOException {
        var eliminated = eliminationManager.attemptElimination(me.user(), code);
        scoreboardController.pushKill(new Kill(me.user(), eliminated));
        response.sendRedirect("/");
    }

    @GetMapping("/target")
    @SentrySpan
    public EliminationUser target(@AuthenticationPrincipal EliminationAuthentication me) {
        return me.user().getTarget();
    }
}
