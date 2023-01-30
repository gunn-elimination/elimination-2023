package net.gunn.elimination;

import net.gunn.elimination.auth.EliminationCodeGenerator;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.routes.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static net.gunn.elimination.auth.Roles.PLAYER;

@Service
@Transactional
@ConfigurationPropertiesScan
public class EliminationManager {
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;

    private final Instant gameStartTime, gameEndTime;
	private final EliminationCodeGenerator eliminationCodeGenerator;

    public EliminationManager(
        UserRepository userRepository,
        EntityManager entityManager,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Value("${elimination.game-start-time}") LocalDateTime gameStartTime,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Value("${elimination.game-end-time}") LocalDateTime gameEndTime,
		EliminationCodeGenerator eliminationCodeGenerator) {
        this.userRepository = userRepository;
        this.entityManager = entityManager;

        this.gameStartTime = gameStartTime.toInstant(ZonedDateTime.now().getOffset());
        this.gameEndTime = gameEndTime.toInstant(ZonedDateTime.now().getOffset());
		this.eliminationCodeGenerator = eliminationCodeGenerator;
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
}
