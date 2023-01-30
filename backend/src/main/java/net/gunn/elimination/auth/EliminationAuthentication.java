package net.gunn.elimination.auth;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.io.Serializable;

public interface EliminationAuthentication extends AuthenticatedPrincipal, OidcUser, Serializable {
	String subject();
}
