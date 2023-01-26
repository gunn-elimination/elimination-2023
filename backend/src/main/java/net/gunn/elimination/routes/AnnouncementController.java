package net.gunn.elimination.routes;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.auth.EliminationAuthentication;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.repository.AnnouncementRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.gunn.elimination.auth.Roles.ADMIN;

@RestController
@CrossOrigin(originPatterns = "*")
public class AnnouncementController {
	private final AnnouncementRepository announcementRepository;
	private final Set<SseEmitter> emitters = ConcurrentHashMap.newKeySet();

	private final EntityManagerFactory emf;

	public AnnouncementController(AnnouncementRepository announcementRepository, EntityManagerFactory emf) {
		this.announcementRepository = announcementRepository;
		this.emf = emf;
	}

	@GetMapping(value = "/announcements", produces = "application/json")
	@SentrySpan
	public List<Announcement> announcements() {
		List<Announcement> result;
		if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof EliminationAuthentication auth
			&& auth.user().getRoles().contains(ADMIN))
			result = announcementRepository.findAll();
		else
			result = announcementRepository.findAnnouncementsForCurrentTime();

		result.sort(Comparator.comparing(Announcement::getStartDate).reversed());
		return result;
	}

	@GetMapping(value = "/announcements", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter announcementsStream() throws IOException {
		var em = emf.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();

		try {
			var emitter = new SseEmitter(-1L);

			emitters.add(emitter);

			emitter.send(announcements());

			emitter.onCompletion(() -> emitters.remove(emitter));
			return emitter;
		} finally {
			em.getTransaction().rollback();
			em.close();
		}
	}

	public void pushAnnouncement(Announcement announcement) {
		var allAnnouncements = announcements();
		for (var emitter : new HashSet<>(emitters)) {
			try {
				emitter.send(allAnnouncements);
			} catch (IOException e) {
				// Ignore
			}
		}
	}
}
