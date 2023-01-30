package net.gunn.elimination.routes.announcements;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.sentry.spring.jakarta.tracing.SentrySpan;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.web.servlet.mvc.method.annotation.SseEmitter.event;

@RestController
@CrossOrigin(originPatterns = "*")
class AnnouncementController {
	private final AnnouncementService announcementService;
	private final Set<SseEmitter> announcementEmitters = ConcurrentHashMap.newKeySet();

	public AnnouncementController(AnnouncementService announcementService) {
		this.announcementService = announcementService;
	}

	@GetMapping(value = "/announcements", produces = "application/json")
	@SentrySpan
	@ResponseBody
	public List<Announcement> getAnnouncements(ObjectMapper objectMapper) {
		return announcementService.announcementsVisibleByCurrentlyAuthenticatedUser();
	}

	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	@GetMapping(value = "/announcements", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter getAnnouncementsStream() throws IOException {
		var emitter = new SseEmitter(-1L);
		emitter.onCompletion(() -> announcementEmitters.remove(emitter));
		emitter.onTimeout(() -> announcementEmitters.remove(emitter));

		announcementEmitters.add(emitter);
		emitter.send(event().data(announcementService.announcementsVisibleByAllUsers()));
		return emitter;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/announcement")
	public void createAnnouncement(
		HttpServletRequest req,
		HttpServletResponse response,
		@RequestParam("title") String title,
		@RequestParam("body") String body,
		@RequestParam long startTime,
		@RequestParam(required = false, defaultValue = "253402329599000") long endTime,
		@RequestParam(required = false) String active
	) throws IOException {
		var announcement = new Announcement(title, body, new java.sql.Date(startTime), new java.sql.Date(endTime), active != null && active.equals("on"));
		announcementService.saveAnnouncement(announcement);

		response.setStatus(HttpServletResponse.SC_CREATED);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/announcement/{id}")
	public void deleteAnnouncement(
		HttpServletRequest req,
		HttpServletResponse response,
		@PathVariable("id") int id
	) throws IOException {
		announcementService.deleteAnnouncement(id);

		response.setStatus(HttpServletResponse.SC_CREATED);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/announcement/{id}")
	public void updateAnnouncement(
		HttpServletRequest req,
		HttpServletResponse response,
		@PathVariable("id") int id,
		@RequestParam("title") String title,
		@RequestParam("body") String body,
		@RequestParam long startTime,
		@RequestParam(required = false, defaultValue = "253402329599000") long endTime,
		@RequestParam(required = false) String active
	) throws IOException {
		var announcement = new Announcement(title, body, new java.sql.Date(startTime), new java.sql.Date(endTime), active != null && active.equals("on"));
		announcementService.updateAnnouncement(id, announcement);

		response.setStatus(HttpServletResponse.SC_OK);
		response.sendRedirect(req.getHeader("Referer"));
	}

	@RabbitListener(bindings =
	@QueueBinding(
		value = @Queue(),
		exchange = @Exchange(name = "announcements", type = "fanout")
	)
	)
	@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
	void sendAnnouncementsToConnectedClients() {
		var announcements = announcementService.announcementsVisibleByAllUsers();

		for (var emitter : new HashSet<>(announcementEmitters)) {
			try {
				emitter.send(event().data(announcements));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
