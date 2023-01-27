package net.gunn.elimination.routes.admin;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.repository.AnnouncementRepository;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.AnnouncementController;
import net.gunn.elimination.routes.SSEController;
import net.gunn.elimination.routes.game.Kill;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final AnnouncementRepository announcementRepository;
    private final EliminationManager eliminationManager;
    private final UserRepository userRepository;

    private final AnnouncementController announcementController;
	private final Optional<SSEController> sseController;

    public AdminController(AnnouncementRepository announcementRepository, EliminationManager eliminationManager, UserRepository userRepository, AnnouncementController announcementController, Optional<SSEController> sseController) {
        this.announcementRepository = announcementRepository;
        this.eliminationManager = eliminationManager;
        this.userRepository = userRepository;
        this.announcementController = announcementController;
		this.sseController = sseController;
	}

    @PostMapping("/announcement")
	@Transactional
    public void announcement(
        HttpServletRequest req,
        HttpServletResponse response,
        @RequestParam("title") String title,
        @RequestParam("body") String body,
        @RequestParam long startTime,
        @RequestParam(required = false, defaultValue = "253402329599000") long endTime,
        @RequestParam(required = false) String active
    ) throws IOException {
        var announcement = new Announcement(title, body, new Date(startTime), new Date(endTime), active != null && active.equals("on"));
        announcementRepository.save(announcement);
		sseController.ifPresent(SSEController::signalAnnouncementChange);

		response.setStatus(HttpServletResponse.SC_CREATED);
        response.sendRedirect(req.getHeader("Referer"));
    }

    @PostMapping("/announcement/{id}")
	@Transactional
    public void announcement(
        HttpServletRequest req,
        HttpServletResponse response,
        @PathVariable("id") long id,
        @RequestParam("title") String title,
        @RequestParam("body") String body,
        @RequestParam long startTime,
        @RequestParam(required = false, defaultValue = "253402329599000") long endTime,
        @RequestParam(required = false) String active,
        @RequestParam(required = false, defaultValue = "action") String action
    ) throws IOException {
        if (action.equals("delete")) {
            announcementRepository.deleteById(id);
        } else {
            var announcement = announcementRepository.findById(id).orElseThrow();
            announcement.setTitle(title);

            announcement.setBody(body);

            announcement.setStartDate(new Date(startTime));
            announcement.setEndDate(new Date(endTime));

            announcement.setActive(active != null && active.equals("on"));

            announcementRepository.deleteById(id);
            announcementRepository.save(announcement);
			sseController.ifPresent(SSEController::signalAnnouncementChange);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(req.getHeader("Referer"));
    }

    @DeleteMapping("/user/{email}")
	@Transactional
    public void user(
        HttpServletRequest req,
        HttpServletResponse response,
        @PathVariable("email") String email
    ) throws IOException {
        var subject = userRepository.findByEmail(email).orElseThrow().getSubject();
        eliminationManager.unlink(subject);
        userRepository.deleteBySubject(subject);
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(req.getHeader("Referer"));
    }

	@GetMapping("/regenerateCodes")
	@Transactional
	public void regenerateCodes(
		HttpServletRequest req,
		HttpServletResponse response
	) throws IOException {
		eliminationManager.regenerateCodes();
		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@GetMapping("/test/announcement")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testAnnouncement(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalAnnouncementChange();
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}

	@GetMapping("/test/elimination")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testElimination(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalKill(new Kill(new EliminationUser(), new EliminationUser()));
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}

	@GetMapping("/test/scoreboard")
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	public String testScoreboard(HttpServletRequest req, HttpServletResponse response) throws IOException {
		sseController.get().signalScoreboardChange();
		response.setStatus(HttpServletResponse.SC_OK);
		return "OK";
	}
}
