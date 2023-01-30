package net.gunn.elimination.routes.announcements;

import com.github.fridujo.rabbitmq.mock.compatibility.MockConnectionFactoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.test.StepVerifier;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class AnnouncementControllerTest {
	@Mock
	private AnnouncementService announcementService;
	private AnnouncementController announcementController;

	private MockMvc mockMvc;

	private WebTestClient webTestClient;

	@BeforeEach
	void setUp() {
		announcementController = new AnnouncementController(announcementService);
		mockMvc = MockMvcBuilders.standaloneSetup(announcementController).build();
		webTestClient = WebTestClient.bindToController(announcementController).configureClient().build();
	}

	@Test
	void getAnnouncements() throws Exception {
		when(announcementService.announcementsVisibleByCurrentlyAuthenticatedUser()).thenReturn(List.of());
		mockMvc.perform(get("/announcements"))
			.andExpect(status().isOk())
			.andExpect(content().json("[]"));

		var tomorrow = Date.valueOf(LocalDate.now().plus(1, ChronoUnit.DAYS));
		var yesterday = Date.valueOf(LocalDate.now().minus(1, ChronoUnit.DAYS));

		var announcement1 = new Announcement("hello", "test", yesterday, tomorrow, true);
		var announcement2 = new Announcement("hello2", "test2", yesterday, tomorrow, false);
		var announcement3 = new Announcement("hello3", "test3", yesterday, yesterday, true);

		announcement1.id = 1L;
		announcement2.id = 2L;
		announcement3.id = 3L;

		when(announcementService.announcementsVisibleByCurrentlyAuthenticatedUser()).thenReturn(List.of(announcement1, announcement2, announcement3));
		mockMvc.perform(get("/announcements").accept("application/json"))
			.andExpect(content().json("""
				[
					{
						"id": 1,
						"title": "hello",
						"body": "test",
						"startDate": %s,
						"endDate": %s
					},
					{
						"id": 2,
						"title": "hello2",
						"body": "test2",
						"startDate": %s,
						"endDate": %s
					},
					{
						"id": 3,
						"title": "hello3",
						"body": "test3",
						"startDate": %s,
						"endDate": %s
					}
					]
				""".formatted(yesterday.getTime(), tomorrow.getTime(), yesterday.getTime(), tomorrow.getTime(), yesterday.getTime(), yesterday.getTime())));
	}

	@Test
	void getAnnouncementsStream() throws InterruptedException {
		var tomorrow = Date.valueOf(LocalDate.now().plus(1, ChronoUnit.DAYS));
		var yesterday = Date.valueOf(LocalDate.now().minus(1, ChronoUnit.DAYS));

		var announcement1 = new Announcement("hello", "test", yesterday, tomorrow, true);
		var announcement2 = new Announcement("hello2", "test2", yesterday, tomorrow, false);
		var announcement3 = new Announcement("hello3", "test3", yesterday, yesterday, true);
		when(announcementService.announcementsVisibleByCurrentlyAuthenticatedUser()).thenReturn(List.of(announcement1, announcement2, announcement3));
		when(announcementService.announcementsVisibleByAllUsers()).thenReturn(List.of(announcement1));

		var response = webTestClient.get().uri("/announcements")
			.accept(TEXT_EVENT_STREAM)
			.exchange()
			.expectStatus().isOk()
			.expectHeader().contentTypeCompatibleWith(TEXT_EVENT_STREAM)
			.returnResult(ServerSentEvent.class);
		response. getResponseBody().log().subscribe();

	}

	@Test
	void createAnnouncement() {
	}

	@Test
	void deleteAnnouncement() {
	}

	@Test
	void updateAnnouncement() {
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