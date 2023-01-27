package net.gunn.elimination.routes;

import io.sentry.Sentry;
import io.sentry.spring.tracing.SentrySpan;
import net.gunn.elimination.model.Announcement;
import net.gunn.elimination.routes.game.Kill;
import net.gunn.elimination.routes.game.ScoreboardController;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@CrossOrigin(originPatterns = "*")
@ConditionalOnProperty(name = "elimination.sse.enabled", havingValue = "true")
public class SSEController {
	private final RabbitTemplate rabbitTemplate; // so we can broadcast between replicas

	private final AnnouncementController announcementController;
	private final ScoreboardController scoreboardController;

	private final Set<SseEmitter> announcementEmitters = ConcurrentHashMap.newKeySet();
	private final Set<ScoreboardController.ScoreboardSubscription> scoreboardEmitters = ConcurrentHashMap.newKeySet();
	private final Set<SseEmitter> killEmitters = ConcurrentHashMap.newKeySet();

	private final EntityManagerFactory emf;

	public SSEController(RabbitTemplate rabbitTemplate, AnnouncementController announcementController, ScoreboardController scoreboardController, EntityManagerFactory emf) {
		this.rabbitTemplate = rabbitTemplate;
		this.announcementController = announcementController;
		this.scoreboardController = scoreboardController;
		this.emf = emf;
	}

	@GetMapping(value = "/announcements", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter announcementsStream() throws IOException {
		var emitter = new SseEmitter(-1L);

		announcementEmitters.add(emitter);

		var em = emf.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();
		try {
			emitter.send(announcementController.announcements());
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

		emitter.onCompletion(() -> announcementEmitters.remove(emitter));
		return emitter;
	}

	@GetMapping(value = "/game/scoreboard", produces = "text/event-stream")
	@ResponseBody
	public SseEmitter scoreboardStream(@RequestParam(defaultValue = "20") int limit) throws IOException {
		var em = emf.createEntityManager();
		var emitter = new SseEmitter(-1L);

		var sub = new ScoreboardController.ScoreboardSubscription(emitter, limit);
		scoreboardEmitters.add(sub);
		emitter.onCompletion(() -> scoreboardEmitters.remove(sub));

		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();
		try {
			emitter.send(scoreboardController.scoreboard(limit));
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

		return emitter;
	}

	@GetMapping(value = "/game/eliminations", produces = "text/event-stream")
	public SseEmitter kills() {
		var emitter = new SseEmitter(-1L);

		killEmitters.add(emitter);
		emitter.onCompletion(() -> killEmitters.remove(emitter));

		return emitter;
	}

	@RabbitListener(queues = "announcements")
	public void sendAnnouncementsToConnectedClients() {
		List<Announcement> announcements;

		var em = emf.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();
		try {
			announcements = announcementController.announcements();
		} catch (Exception e) {
			Sentry.captureException(e);
			return;
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

		for (var emitter : new HashSet<>(announcementEmitters)) {
			try {
				emitter.send(announcements);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SentrySpan(description = "send scoreboard after update via sse")
	@RabbitListener(queues = "scoreboard")
	public void sendScoreboardToConnectedClients() {
		var em = emf.createEntityManager();
		em.getTransaction().begin();
		em.getTransaction().setRollbackOnly();

		ScoreboardController.Scoreboard scoreboard;
		try {
			var maxLimit = new HashSet<>(scoreboardEmitters).stream().max(Comparator.comparingInt(ScoreboardController.ScoreboardSubscription::limit)).get().limit();
			scoreboard = scoreboardController.scoreboard(maxLimit);
		} catch (Exception e) {
			Sentry.captureException(e);
			return;
		} finally {
			em.getTransaction().rollback();
			em.close();
		}

		for (var sub : new HashSet<>(scoreboardEmitters)) {
			try {
				var sublist = scoreboard.users().subList(0, Math.min(sub.limit(), scoreboard.users().size()));
				sub.emitter().send(new ScoreboardController.Scoreboard(sublist));
			} catch (IOException e) {
				// ignore, spring should call onCompletion
			}
		}
	}

	@SentrySpan(description = "sends kill events to clients via see")
	@RabbitListener(queues = "kills")
	void sendKillToConnectedClients(Kill kill) {
		for (var emitter : new HashSet<>(killEmitters)) {
			try {
				emitter.send(kill);
			} catch (IOException e) {
				// ignore
			}
		}
	}

	public void signalAnnouncementChange() {
		rabbitTemplate.convertAndSend("announcements", "");
	}

	public void signalScoreboardChange() {
		rabbitTemplate.convertAndSend("scoreboard", "");
	}

	public void signalKill(Kill kill) {
		rabbitTemplate.convertAndSend("kills", kill);
	}

	@Bean
	public Declarables sseDeclarables() {
		return new Declarables(
			new Queue("announcements", true, false, false),
			new Queue("scoreboard", true, false, false),
			new Queue("kills", true, false, false),
			new FanoutExchange("sse"),
			BindingBuilder.bind(new Queue("announcements")).to(new FanoutExchange("sse")),
			BindingBuilder.bind(new Queue("scoreboard")).to(new FanoutExchange("sse")),
			BindingBuilder.bind(new Queue("kills")).to(new FanoutExchange("sse"))
		);
	}
}
