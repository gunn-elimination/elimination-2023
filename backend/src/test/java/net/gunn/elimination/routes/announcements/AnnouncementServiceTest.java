package net.gunn.elimination.routes.announcements;

import com.github.fridujo.rabbitmq.mock.MockConnectionFactory;
import com.github.fridujo.rabbitmq.mock.compatibility.MockConnectionFactoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static net.gunn.elimination.auth.Roles.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AnnouncementServiceTest {
	@Mock
	private AnnouncementRepository announcementRepository;
	@Mock
	private Authentication authentication;
	@Mock
	private MessageListener messageListener;

	@Autowired
	RabbitAdmin rabbitAdmin;
	@Autowired
	ConnectionFactory cf;
	@Autowired
	RabbitTemplate rabbitTemplate;

	private AnnouncementService announcementService;
	@BeforeEach
	public void setup() {
		announcementService = new AnnouncementService(rabbitTemplate, announcementRepository, authentication);
		var exchange = ExchangeBuilder.fanoutExchange("announcements").build();
		var announcementQueue = new Queue("testAnnouncementQueue");

		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.declareQueue(announcementQueue);
		rabbitAdmin.declareBinding(BindingBuilder.bind(announcementQueue).to(exchange).with("").noargs());

		var container = new SimpleMessageListenerContainer();
		container.setQueues(announcementQueue);
		container.setMessageListener(messageListener);
		container.setConnectionFactory(cf);
		container.start();
	}

	@Test
	public void testAnnouncementsVisibleByCurrentlyAuthenticatedUser() {
		var publicAnnouncement = mock(Announcement.class);
		var adminAnnouncement = mock(Announcement.class);
		when(announcementRepository.findActiveAnnouncements()).thenReturn(List.of(publicAnnouncement));
		when(announcementRepository.findAllAnnouncements()).thenReturn(List.of(publicAnnouncement, adminAnnouncement));

		var mockAuthentication = mock(Authentication.class);
		SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

		when(mockAuthentication.getAuthorities().contains(ADMIN)).thenReturn(true);
		var announcements = announcementService.announcementsVisibleByCurrentlyAuthenticatedUser();
		assertEquals(announcements.size(), 2);
		assertTrue(announcements.contains(publicAnnouncement));
		assertTrue(announcements.contains(adminAnnouncement));

		when(mockAuthentication.getAuthorities().contains(ADMIN)).thenReturn(false);
		announcements = announcementService.announcementsVisibleByCurrentlyAuthenticatedUser();
		assertEquals(announcements.size(), 1);
		assertTrue(announcements.contains(publicAnnouncement));
		assertFalse(announcements.contains(adminAnnouncement));
	}

	@Test
	public void testAnnouncementsVisibleByAllUsers() {
		var publicAnnouncement = mock(Announcement.class);
		var adminAnnouncement = mock(Announcement.class);
		when(announcementRepository.findActiveAnnouncements()).thenReturn(List.of(publicAnnouncement, adminAnnouncement));

		var announcements = announcementService.announcementsVisibleByAllUsers();
		assertEquals(announcements.size(), 2);
		assertTrue(announcements.contains(publicAnnouncement));
		assertTrue(announcements.contains(adminAnnouncement));
	}

	@Test
	public void testSaveAnnouncement() throws InterruptedException {
		var announcement = mock(Announcement.class);
		var announcement2 = mock(Announcement.class);

		announcementService.saveAnnouncement(announcement);
		verify(announcementRepository).save(announcement);

		announcementService.saveAnnouncement(announcement2);
		verify(announcementRepository).save(announcement2);

		Thread.sleep(100);
		verify(messageListener, times(2)).onMessage(notNull());
	}

	@Test
	public void testDeleteAnnouncement() {
		var announcement = mock(Announcement.class);
		announcementService.deleteAnnouncement((int) announcement.getID());
		verify(announcementRepository).deleteById((int) announcement.getID());

	}

	@Test
	public void testUpdateAnnouncement() {
		var announcement = mock(Announcement.class);
		when(announcementRepository.findById((int) announcement.getID())).thenReturn(Optional.of(announcement));

		announcementService.updateAnnouncement((int) announcement.getID(), announcement);
		verify(announcementRepository).save(announcement);
	}

	@Configuration
	public static class RabbitTestConfig {
		@Bean
		ConnectionFactory connectionFactory() {
			return new CachingConnectionFactory(
				MockConnectionFactoryFactory
					.build()
					.enableConsistentHashPlugin()
			);
		}

		@Bean
		RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
			return new RabbitAdmin(connectionFactory);
		}

		@Bean
		RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
			return new RabbitTemplate(connectionFactory);
		}
	}
}