package net.gunn.elimination.routes;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.routes.game.ScoreboardController;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@RequestMapping("/")
@Controller
public class Index {
    private final EliminationManager eliminationManager;
    private final ScoreboardController scoreboardController;
    private final AnnouncementController announcementController;

    public Index(EliminationManager eliminationManager, ScoreboardController scoreboardController, AnnouncementController announcementController) {
        this.eliminationManager = eliminationManager;
        this.scoreboardController = scoreboardController;
        this.announcementController = announcementController;
    }

    @RequestMapping("/")
    public String index(@AuthenticationPrincipal EliminationAuthentication user, Model model) {
        if (user == null)
            return "redirect:/login";

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth)
            model.addAttribute("currentUser", auth.user());

        model.addAttribute("eliminationManager", eliminationManager);
        if (eliminationManager.gameHasStarted())
            model.addAttribute("scoreboard", scoreboardController.scoreboard(20));
        model.addAttribute("roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        model.addAttribute("announcements", announcementController.announcements());
        model.addAttribute("winner", eliminationManager.getWinner());
        return "index";
    }
}
