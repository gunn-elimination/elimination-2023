package net.gunn.elimination.routes;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.repository.AnnouncementRepository;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static net.gunn.elimination.auth.Roles.ADMIN;

@RestController
@CrossOrigin(originPatterns = "*")
public class AnnouncementController {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository, Optional<RabbitTemplate> rabbitTemplate) {
        this.announcementRepository = announcementRepository;
	}

    @GetMapping(value = "/announcements", produces = "application/json")
    @SentrySpan
    public List<Announcement> announcements() {
        List<Announcement> result;
        if (SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth
            && auth.user().getRoles().contains(ADMIN))
            result = announcementRepository.findAll();
        else
            result = announcementRepository.findAnnouncementsForCurrentTime();

        result.sort(Comparator.comparing(Announcement::getStartDate).reversed());
        return result;
    }
}
