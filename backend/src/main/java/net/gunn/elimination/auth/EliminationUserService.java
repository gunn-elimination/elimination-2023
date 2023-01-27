package net.gunn.elimination.auth;

import net.gunn.elimination.EliminationManager;
import net.gunn.elimination.exceptions.NonPAUSDUserException;
import net.gunn.elimination.model.EliminationUser;
import net.gunn.elimination.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import static net.gunn.elimination.auth.Roles.*;

@Service
@Transactional
class EliminationUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
	private static final Pattern PAUSD_DOMAIN_PATTERN = Pattern.compile("[a-z]{2}[0-9]{5}@pausd\\.us");
	private final Random random = new Random();
	private final OidcUserService delegate;
	private final AdminList admins;
	private final EliminationCodeGenerator eliminationCodeGenerator;
	private final UserRepository userRepository;
	private final LocalDateTime registrationDeadline;
	private final EliminationManager eliminationManager;

	public EliminationUserService(
		UserRepository userRepository,
		EliminationCodeGenerator eliminationCodeGenerator,

		AdminList admins,
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Value("${elimination.registration-deadline}") LocalDateTime registrationDeadline,
		EliminationManager eliminationManager) {
		this.userRepository = userRepository;
		this.eliminationCodeGenerator = eliminationCodeGenerator;

		this.admins = admins;
		this.registrationDeadline = registrationDeadline;
		this.eliminationManager = eliminationManager;
		this.delegate = new OidcUserService();
	}

	private boolean isValidEmail(String email) {
		return admins.isAdmin(email) || PAUSD_DOMAIN_PATTERN.matcher(email).matches();
	}

	@Override
	public EliminationOauthAuthenticationImpl loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = delegate.loadUser(userRequest);
		if (!isValidEmail(oidcUser.getEmail()))
			throw new NonPAUSDUserException(oidcUser.getEmail());

		try {
			return processOidcUser(oidcUser);
		} catch (RegistrationClosedException e) {
			sneakyThrow(e);
			return null;
		}
	}

	<T extends Throwable> T sneakyThrow(Throwable t) throws T {
		throw (T) t;
	}

	protected void insertUserRandomly(EliminationUser user) {
		if (userRepository.count() <= 1) {
			user.setTarget(user);
			user.setTargettedBy(user);
			userRepository.save(user);
			return;
		}

		var page = random.nextInt((int) (userRepository.count() - 1));
		var pageRequest = PageRequest.of(page, 1);
		var insertionPoint = userRepository.findEliminationUsersByRolesContainingAndSubjectNot(
			PLAYER,
			user.getSubject(),
			pageRequest
		).getContent().get(0);

		user.setTarget(insertionPoint.getTarget());
		user.getTarget().setTargettedBy(user);

		insertionPoint.setTarget(user);
		user.setTargettedBy(insertionPoint);

		user.addRole(PLAYER);
		userRepository.save(user);
		userRepository.save(user.getTarget());

		insertionPoint.addRole(PLAYER);
		userRepository.save(insertionPoint);
	}

	private void setupNewUser(OidcUser oidcUser) {
		var user = new EliminationUser(
			oidcUser.getSubject(),
			oidcUser.getEmail(),
			oidcUser.getGivenName(),
			oidcUser.getFamilyName(),
			eliminationCodeGenerator.randomCode(),
			Set.of(USER)
		);
		if (userRepository.count() == 1) {
			// Start the game
			userRepository.findAll().forEach(u -> {
				u.addRole(PLAYER);
				userRepository.save(u);
			});
		}

		userRepository.save(user);

		if (!eliminationManager.gameHasEnded())
			insertUserRandomly(user);
	}

	private EliminationOauthAuthenticationImpl processOidcUser(OidcUser oidcUser) throws RegistrationClosedException {
		if (!userRepository.existsBySubject(oidcUser.getSubject())) {
			if (LocalDateTime.now().isAfter(registrationDeadline))
				throw new RegistrationClosedException();

			setupNewUser(oidcUser);
		}

		var user = userRepository.findBySubject(oidcUser.getSubject()).orElseThrow();
		if (admins.isAdmin(user.getEmail()) && !user.getRoles().contains(ADMIN)) {
			user.addRole(ADMIN);
			user = userRepository.save(user);
		}

		return new EliminationOauthAuthenticationImpl(user, oidcUser);
	}
}