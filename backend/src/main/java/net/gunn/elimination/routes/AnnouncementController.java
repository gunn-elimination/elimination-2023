package net.gunn.elimination.routes;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.repository.AnnouncementRepository;
import net.gunn.elimination.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static net.gunn.elimination.auth.Roles.ADMIN;

@RestController
@CrossOrigin(originPatterns = "*")
public class AnnouncementController {
    private final AnnouncementRepository announcementRepository;
	private final UserRepository userRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository, Optional<RabbitTemplate> rabbitTemplate, UserRepository userRepository) {
        this.announcementRepository = announcementRepository;
		this.userRepository = userRepository;
	}

    @GetMapping(value = "/announcements", produces = "application/json")
    @SentrySpan
    public List<Announcement> announcements() {
        List<Announcement> result;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth
            && userRepository.findBySubject(auth.subject()).orElseThrow().getRoles().contains(ADMIN))
            result = announcementRepository.findAll();
        else
            result = announcementRepository.findAnnouncementsForCurrentTime();

        result.sort(Comparator.comparing(Announcement::getStartDate).reversed());
		Hibernate.initialize(result);
        return result;
    }
}
