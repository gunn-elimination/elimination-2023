package net.gunn.elimination.auth;

import net.gunn.elimination.model.EliminationUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

class EliminationOauthAuthenticationImpl extends DefaultOidcUser implements EliminationAuthentication {
    private final OidcUser original;

    public EliminationOauthAuthenticationImpl(EliminationUser user, OidcUser original) {
        super(user.getRoles().stream().map(Object::toString).map(SimpleGrantedAuthority::new).toList(), original.getIdToken(), original.getUserInfo());
        this.original = original;
    }

    @Override
	public String subject() {
		return original.getSubject();
	}

    public OidcUser original() {
        return original;
    }

    @Override
    public String getName() {
        return subject();
    }

}
