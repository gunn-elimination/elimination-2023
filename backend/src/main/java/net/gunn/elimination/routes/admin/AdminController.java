package net.gunn.elimination.routes.admin;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.repository.AnnouncementRepository;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.AnnouncementController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final AnnouncementRepository announcementRepository;
    private final EliminationManager eliminationManager;
    private final UserRepository userRepository;
    private final AnnouncementController announcementController;

    public AdminController(AnnouncementRepository announcementRepository, EliminationManager eliminationManager, UserRepository userRepository, AnnouncementController announcementController) {
        this.announcementRepository = announcementRepository;
        this.eliminationManager = eliminationManager;
        this.userRepository = userRepository;
        this.announcementController = announcementController;
    }

    @PostMapping("/announcement")
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
        announcementController.pushAnnouncement(announcement);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.sendRedirect(req.getHeader("Referer"));
    }

    @PostMapping("/announcement/{id}")
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
            announcementController.pushAnnouncement(announcement);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(req.getHeader("Referer"));
    }

    @DeleteMapping("/user/{email}")
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
}
