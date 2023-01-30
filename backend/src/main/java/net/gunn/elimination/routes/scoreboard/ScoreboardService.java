package net.gunn.elimination.routes.scoreboard;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static net.gunn.elimination.auth.Roles.EXCLUDED;

@Service
public class ScoreboardService {
	@PersistenceContext
	private EntityManager entityManager;


	public ScoreboardService(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public ScoreboardService() {
	}

	public Scoreboard getScoreboard(int limit) {
		return new Scoreboard(entityManager.createQuery("""
				SELECT new net.gunn.elimination.routes.scoreboard.ScoreboardEntry(
					user,
					COUNT(eliminatedBy)
				)
				FROM EliminationUser user
				LEFT JOIN user.eliminatedBy eliminatedBy
				WHERE (:excluded NOT MEMBER OF user.roles
					   AND user.eliminatedBy IS NULL)
				GROUP BY user
				ORDER BY COUNT(eliminatedBy) DESC
				""", ScoreboardEntry.class)
			.setParameter("excluded", EXCLUDED)
			.setMaxResults(limit)
			.getResultList());
	}
}
