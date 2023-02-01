package net.gunn.elimination.routes;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.game.ScoreboardController;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@RequestMapping("/")
@Controller
public class Index {
    private final EliminationManager eliminationManager;
    private final ScoreboardController scoreboardController;
    private final AnnouncementController announcementController;
	private final UserRepository userRepository;

    public Index(EliminationManager eliminationManager, ScoreboardController scoreboardController, AnnouncementController announcementController, UserRepository userRepository) {
        this.eliminationManager = eliminationManager;
        this.scoreboardController = scoreboardController;
        this.announcementController = announcementController;
		this.userRepository = userRepository;
	}

    @RequestMapping("/")
	@Transactional
    public String index(@AuthenticationPrincipal EliminationAuthentication user, Model model) {
        if (user == null)
            return "redirect:/login";

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth) {
			var user_ = userRepository.findBySubject(auth.subject());
			model.addAttribute("currentUser", user_);
		}

        model.addAttribute("eliminationManager", eliminationManager);
        if (eliminationManager.gameHasStarted())
            model.addAttribute("scoreboard", scoreboardController.scoreboard(20));
        model.addAttribute("roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        model.addAttribute("announcements", announcementController.announcements());
        model.addAttribute("winner", eliminationManager.getWinner());
        return "index";
    }
}
