package net.gunn.elimination.routes.kills;

import io.sentry.spring.jakarta.tracing.SentrySpan;
import net.gunn.elimination.EmptyGameException;
import net.gunn.elimination.IncorrectEliminationCodeException;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.routes.user.UserRepository;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import static net.gunn.elimination.auth.Roles.PLAYER;
import static org.springframework.context.annotation.ScopedProxyMode.TARGET_CLASS;
import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Service
@Scope(scopeName = SCOPE_SESSION, proxyMode = TARGET_CLASS)
public class KillService {
	private final UserRepository userRepository;
	private final RabbitTemplate rabbitTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	public KillService(UserRepository userRepository, RabbitTemplate rabbitTemplate) {
		this.userRepository = userRepository;
		this.rabbitTemplate = rabbitTemplate;
	}

	private void signalKill(Kill kill) {
		rabbitTemplate.convertAndSend("kills", "", kill);
	}

	@Transactional
	public void attemptElimination(String eliminatorSubject, String code) throws IncorrectEliminationCodeException, EmptyGameException {
		var eliminator = userRepository.findBySubject(eliminatorSubject).orElseThrow();
		if (eliminator.getTarget() == null)
			throw new EmptyGameException();

		if (!eliminator.getTarget().getEliminationCode().equals(code))
			throw new IncorrectEliminationCodeException("Incorrect code");

		var target = eliminator.getTarget();

		entityManager.createQuery("""
UPDATE EliminationUser u
SET u.targettedBy = null,
	u.target = null,
	u.eliminationCode = null,
	u.eliminatedBy = :eliminator,
	u.targettedBy.target = u.target
WHERE u = :target
			""")
			.setParameter("eliminator", eliminator)
			.setParameter("target", eliminator.getTarget())
			.executeUpdate();

		// remove player role from target
		{
			var targetUser = userRepository.findBySubject(target.getSubject()).orElseThrow();
			targetUser.getRoles().remove(PLAYER);
			userRepository.save(targetUser);
		}

		signalKill(new Kill(
			userRepository.findBySubject(eliminator.getSubject()).orElseThrow(),
			userRepository.findBySubject(target.getSubject()).orElseThrow()
		));
	}

	void eliminate0(String eliminatorSubject, String toEliminateSubject) {
		var toEliminate = entityManager.find(EliminationUser.class, toEliminateSubject);
		assert !toEliminate.isEliminated();

		var eliminator = entityManager.find(EliminationUser.class, eliminatorSubject);
		eliminator.setTarget(toEliminate.getTarget());
		eliminator.getTarget().setTargettedBy(eliminator);
		eliminator = userRepository.save(eliminator);

		if (eliminator.getTarget().getSubject().equals(eliminator.getSubject())) {
			eliminator.setTarget(null);
			eliminator.setTargettedBy(null);
			eliminator.setEliminationCode(null);
			eliminator.setWinner(true);
			eliminator = userRepository.save(eliminator);
		}

		toEliminate.setTarget(null);
		toEliminate.setTargettedBy(null);
		toEliminate.setEliminatedBy(eliminator);
		toEliminate.setEliminationCode(null);
		toEliminate.removeRole(PLAYER);
		userRepository.save(toEliminate);
	}

	public void unlink(String subject) {
		var user = entityManager.find(EliminationUser.class, subject);
		user.getTargettedBy().setTarget(user.getTarget());
		user.getTarget().setTargettedBy(user.getTargettedBy());

		user.setTarget(null);
		user.setTargettedBy(null);
		user.setEliminatedBy(null);
		user.setEliminationCode(null);
		userRepository.save(user);
	}
}
