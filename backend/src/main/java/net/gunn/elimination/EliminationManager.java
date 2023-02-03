package net.gunn.elimination;

import net.gunn.elimination.auth.EliminationCodeGenerator;
import net.gunn.elimination.auth.EliminationUserService;
import net.gunn.elimination.auth.Roles;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.model.Kill;
import net.gunn.elimination.repository.KillfeedRepository;
import net.gunn.elimination.repository.UserRepository;
import net.gunn.elimination.routes.SSEController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import static net.gunn.elimination.auth.Roles.PLAYER;
import static net.gunn.elimination.auth.Roles.USER;

@Service
@Transactional
@ConfigurationPropertiesScan
public class EliminationManager {
	@PersistenceContext
	private final EntityManager entityManager;
	private final UserRepository userRepository;

	private final Instant gameStartTime, gameEndTime;
	private final EliminationCodeGenerator eliminationCodeGenerator;
	private final Optional<SSEController> sseController;
	private final KillfeedRepository killfeedRepository;

	private final Random random = new Random();

	public EliminationManager(
		UserRepository userRepository,
		EntityManager entityManager,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Value("${elimination.game-start-time}") LocalDateTime gameStartTime,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Value("${elimination.game-end-time}") LocalDateTime gameEndTime,
		EliminationCodeGenerator eliminationCodeGenerator,
		Optional<SSEController> sseController, KillfeedRepository killfeedRepository) {
		this.userRepository = userRepository;
		this.entityManager = entityManager;

		this.gameStartTime = gameStartTime.toInstant(ZonedDateTime.now().getOffset());
		this.gameEndTime = gameEndTime.toInstant(ZonedDateTime.now().getOffset());
		this.eliminationCodeGenerator = eliminationCodeGenerator;

		this.sseController = sseController;
		this.killfeedRepository = killfeedRepository;
	}

	public EliminationUser attemptElimination(EliminationUser eliminator, String code) throws IncorrectEliminationCodeException, EmptyGameException {
		var expectedCode = entityManager.createQuery("SELECT u.target.eliminationCode from EliminationUser u WHERE u.subject = :subject", String.class)
			.setParameter("subject", eliminator.getSubject())
			.getResultList();
		if (expectedCode.isEmpty())
			throw new EmptyGameException("Game is not ongoing");

		if (!expectedCode.get(0).equals(code))
			throw new IncorrectEliminationCodeException("Incorrect code");

		var victim = eliminator.getTarget();
		eliminate0(eliminator.getSubject(), victim.getSubject());

		Kill kill = new Kill(eliminator, victim, Instant.now());
		killfeedRepository.save(kill);

		sseController.ifPresent(sseController -> {
			sseController.signalKill(kill);
			sseController.signalScoreboardChange();
		});

		return victim;
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

	public boolean gameHasEnded() {
		return Instant.now().isAfter(gameEndTime) || userRepository.findByWinnerTrue().isPresent();
	}

	public boolean gameHasStarted() {
		return Instant.now().isAfter(gameStartTime);
	}

	public boolean gameHasEnoughPlayers() {
		return userRepository.count() > 1;
	}

	public boolean gameIsOngoing() {
		return gameHasStarted() && !gameHasEnded() && gameHasEnoughPlayers() && userRepository.findByWinnerTrue().isEmpty();
	}

	public EliminationUser getWinner() {
		if (gameIsOngoing())
			return null;

		return entityManager.createQuery("SELECT u FROM EliminationUser u WHERE u.winner = true", EliminationUser.class)
			.getResultList()
			.stream()
			.findFirst()
			.orElse(null);
	}

	public void regenerateCodes() {
		var users = entityManager.createQuery("SELECT u FROM EliminationUser u WHERE u.eliminationCode IS NOT NULL", EliminationUser.class)
			.getResultList();
		for (var user : users) {
			user.setEliminationCode(eliminationCodeGenerator.randomCode());
			userRepository.save(user);
		}
	}

	public void insertUserToChain(EliminationUser user) {
		if (userRepository.count() <= 1) {
			user.setTarget(user);
			user.setTargettedBy(user);
			userRepository.save(user);
			return;
		}

		var page = random.nextInt(userRepository.countEliminationUsersByRolesContaining(PLAYER) - 1);
		var pageRequest = PageRequest.of(page, 1);
		var insertionPoint = userRepository.findEliminationUsersByRolesContainingAndSubjectNot(
			PLAYER,
			user.getSubject(),
			pageRequest
		).getContent().get(0);

		insertUserToChain(user, insertionPoint);
	}

	/**
	 * Inserts a user into the elimination chain given a user insertionPoint, who will be assigned the inserted user
	 * as their new target.
	 *
	 * @param user
	 * @param insertionPoint
	 */
	public void insertUserToChain(EliminationUser user, EliminationUser insertionPoint) {
		user.setTarget(insertionPoint.getTarget());
//		user.getTarget().setTargettedBy(user);

		insertionPoint.setTarget(user);
//		user.setTargettedBy(insertionPoint);

		user.addRole(PLAYER);
		userRepository.save(user);
		userRepository.save(user.getTarget());

		insertionPoint.addRole(PLAYER);
		userRepository.save(insertionPoint);
	}

	public void reshuffleChain() {
		HashMap<EliminationUser, EliminationUser> currentMappings = new HashMap<>();
		Set<EliminationUser> users = userRepository.findEliminationUsersByRolesContaining(PLAYER);

		for (EliminationUser user : users) {
			currentMappings.put(user, user.getTarget());
		}

		for (EliminationUser user : users) {
			while (user.getTarget() == currentMappings.get(user)) {
				insertUserToChain(user);
			}
		}
	}

	public Map validateGame() {
		boolean valid = true;

		Set<EliminationUser> allUsers = userRepository.findEliminationUsersByRolesContaining(USER);

		Set<EliminationUser> visitedActiveUsers = new HashSet<>();
		Set<EliminationUser> eliminated = new HashSet<>();

		for (EliminationUser user : allUsers) {
			if (user.isEliminated()) {
				// make sure not in the elimination chain.. lol
				if (visitedActiveUsers.contains(user)) {
					valid = false;
				}

				eliminated.add(user);
			} else {
				// check if user is already in chain
				if (visitedActiveUsers.contains(user)) {
					// all gucchi
				} else {
					// if not, this should be our first chain
					// otherwise we'll have separate chains which is Bad
					if (visitedActiveUsers.size() > 0) {
						// check that we're in the chain
						if (!visitedActiveUsers.contains(user)) {
							valid = false;
						}

						// add all active users anyway for accurate counts
						visitedActiveUsers.add(user);
					} else {
						// traverse chain and mark all users as visited
						visitedActiveUsers.add(user);

						EliminationUser current = user.getTarget();
						while (current != user) { // keep going until back at start user
							// add to list of active users
							// if already in there, something is wrong
							if (!visitedActiveUsers.add(current)) {
								valid = false;
							}

							// go next
							current = current.getTarget();
						}
					}
				}
			}
		}

//		List<String> activeChain = new ArrayList<>();
//		for (EliminationUser user : visitedActiveUsers) {
//			activeChain.add(user.getForename() + " " + user.getSurname() + "->" + user.getTarget().getForename() + " " + user.getTarget().getSurname());
//		}

		return Map.of(
			"users", allUsers.size(),
			"eliminated", eliminated.size(),
			"active", visitedActiveUsers.size(),
			"valid game", valid
//			"elim chain", activeChain
		);
	}
}
