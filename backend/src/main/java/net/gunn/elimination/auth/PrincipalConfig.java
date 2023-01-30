package net.gunn.elimination.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

import static org.springframework.web.context.WebApplicationContext.SCOPE_SESSION;

@Configuration
public class PrincipalConfig {
	@Bean
	@Scope(scopeName = SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
	public Authentication authentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
