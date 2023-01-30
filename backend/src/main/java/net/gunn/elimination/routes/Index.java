package net.gunn.elimination.routes;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.routes.scoreboard.ScoreboardService;
import net.gunn.elimination.routes.user.UserRepository;
import net.gunn.elimination.routes.announcements.AnnouncementService;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
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
    private final ScoreboardService scoreboardService;
    private final AnnouncementService announcementService;
	private final UserRepository userRepository;

    public Index(EliminationManager eliminationManager, ScoreboardService scoreboardService, AnnouncementService announcementService, UserRepository userRepository) {
        this.eliminationManager = eliminationManager;
        this.scoreboardService = scoreboardService;
		this.announcementService = announcementService;
		this.userRepository = userRepository;
	}

    @RequestMapping("/")
	@Transactional
    public String index(Authentication user, Model model) {
        if (user == null || !user.isAuthenticated())
            return "redirect:/login";

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth) {
			var user_ = userRepository.findBySubject(auth.subject()).orElseThrow();
			model.addAttribute("currentUser", user_);
		}

        model.addAttribute("eliminationManager", eliminationManager);
        if (eliminationManager.gameHasStarted())
            model.addAttribute("scoreboard", scoreboardService.getScoreboard(20));
        model.addAttribute("roles", SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        model.addAttribute("announcements", announcementService.announcementsVisibleByCurrentlyAuthenticatedUser());
        model.addAttribute("winner", eliminationManager.getWinner());
        return "index";
    }
}
