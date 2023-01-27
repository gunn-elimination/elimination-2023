package net.gunn.elimination;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class RabbitConfig {
	@Value("announcements-${random.uuid}")
	private String announcementQueueName;

	@Value("elimination-${random.uuid}")
	private String killQueueName;

	@Value("scoreboard-${random.uuid}")
	private String scoreboardQueueName;

	public String getAnnouncementQueueName() {
		return announcementQueueName;
	}

	public String getKillQueueName() {
		return killQueueName;
	}

	public String getScoreboardQueueName() {
		return scoreboardQueueName;
	}

	@Bean
	public Declarables sseDeclarables() {
		var announcementQueue = new Queue(announcementQueueName, false, false, true);
		var killQueue = new Queue(killQueueName, false, false, true);
		var scoreboardQueue = new Queue(scoreboardQueueName, false, false, true);

		var eliminationExchange = new FanoutExchange("kills");
		var announcementExchange = new FanoutExchange("announcements");
		var scoreboardExchange = new FanoutExchange("scoreboard");

		return new Declarables(
			announcementQueue,
			killQueue,
			scoreboardQueue,
			announcementExchange,
			eliminationExchange,
			scoreboardExchange,
			BindingBuilder.bind(announcementQueue).to(announcementExchange),
			BindingBuilder.bind(killQueue).to(eliminationExchange),
			BindingBuilder.bind(scoreboardQueue).to(scoreboardExchange)
		);
	}

}
