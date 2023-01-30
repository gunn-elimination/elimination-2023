package net.gunn.elimination.routes.announcements;

import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.auth.EliminationAuthentication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static net.gunn.elimination.auth.Roles.ADMIN;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Service
public class AnnouncementService {
	private final RabbitTemplate rabbitTemplate;

	private final AnnouncementRepository announcementRepository;
	private final Authentication authentication;

	public AnnouncementService(RabbitTemplate rabbitTemplate, AnnouncementRepository announcementRepository, @AuthenticationPrincipal Authentication authentication) {
		this.rabbitTemplate = rabbitTemplate;
		this.announcementRepository = announcementRepository;
		this.authentication = authentication;
	}

	/**
	 * @return a list of announcements, sorted by start date, with the most recent first.  If the user is an admin, all announcements are returned.  Otherwise, only announcements that are viewable by the user are returned.
	 */
	@SentrySpan
	public List<Announcement> announcementsVisibleByCurrentlyAuthenticatedUser() {
		return authentication.getAuthorities().contains(ADMIN) ?
			announcementRepository.findAllAnnouncements() :
			announcementRepository.findActiveAnnouncements();
	}

	/**
	 * @return a list of announcements, sorted by start date, with the most recent first.  Only announcements that are viewable by all users are returned.
	 */
	@SentrySpan
	public List<Announcement> announcementsVisibleByAllUsers() {
		return announcementRepository.findActiveAnnouncements();
	}

	/**
	 * saves an announcement to the database
	 * @param announcement the announcement to save
	 */
	@SentrySpan
	public void saveAnnouncement(Announcement announcement) {
		announcementRepository.save(announcement);
		signalAnnouncementChange();
	}

	/**
	 * deletes an announcement from the database by id
	 * @param id the id of the announcement to delete
	 */
	@SentrySpan
	public void deleteAnnouncement(int id) {
		announcementRepository.deleteById(id);
		signalAnnouncementChange();
	}

	/**
	 * updates an announcement in the database by id
	 * @param id the id of the announcement to update
	 * @param announcement the announcement to update
	 */
	@SentrySpan
	@Transactional
	public void updateAnnouncement(int id, Announcement announcement) {
		var oldAnnouncement = announcementRepository.findById(id).orElseThrow();

		oldAnnouncement.setTitle(announcement.getTitle());
		oldAnnouncement.setBody(announcement.getBody());
		oldAnnouncement.setStartDate(announcement.getStartDate());
		oldAnnouncement.setEndDate(announcement.getEndDate());
		oldAnnouncement.setActive(announcement.isActive());

		announcementRepository.save(oldAnnouncement);
		signalAnnouncementChange();
	}

	private void signalAnnouncementChange() {
		rabbitTemplate.convertAndSend("announcements", "");
	}
}
