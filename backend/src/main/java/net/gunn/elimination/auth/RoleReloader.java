package net.gunn.elimination.auth;

import net.gunn.elimination.routes.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import jakarta.servlet.*;
import java.io.IOException;

//@Service
class RoleReloader implements Filter {
    private final UserRepository userRepository;

    public RoleReloader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var ctx = SecurityContextHolder.getContext();

        if (ctx.getAuthentication().getPrincipal() instanceof EliminationOauthAuthenticationImpl currentUser) {
            var oldAuth = ((OAuth2AuthenticationToken) ctx.getAuthentication());
            var updatedUser = userRepository.findBySubject(currentUser.getSubject()).orElseThrow();
            var newAuth = new EliminationOauthAuthenticationImpl(updatedUser, currentUser.original());
            var token = new OAuth2AuthenticationToken(newAuth, newAuth.getAuthorities(), oldAuth.getAuthorizedClientRegistrationId());
            ctx.setAuthentication(token);
        }

        chain.doFilter(request, response);
    }
}
