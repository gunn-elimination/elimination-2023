package net.gunn.elimination.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import net.gunn.elimination.routes.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

import java.io.IOException;
import java.util.Map;

@Configuration
@EnableJdbcHttpSession
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class WebSecurityConfig {
    private final EliminationUserService userDetailsService;
	private final ObjectMapper mapper;

    public WebSecurityConfig(EliminationUserService userDetailsService, Error errorHandler, ObjectMapper mapper) {
        this.userDetailsService = userDetailsService;
		this.mapper = mapper;
    }

    @Bean
    @Autowired
    protected SecurityFilterChain webSecurityCustomizer(
        HttpSecurity http
    ) throws Exception {
        return http
            .authorizeRequests()
            .anyRequest()
            .permitAll()
            .and()
            .oauth2Login(n -> n.userInfoEndpoint().oidcUserService(userDetailsService)
                .and()
                .successHandler((request, response, authentication) -> response.sendRedirect(request.getContextPath() + "/login/success"))
                .failureHandler((request, response, authException) -> {
                    response.setContentType("application/json");
                    request.getSession().invalidate();
                    response.getOutputStream().println(
                        mapper.writeValueAsString(Map.of("error", Map.of("code", 403, "message", authException.getMessage())))
                    );
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                })
                .loginPage("/login")
            )
            .headers().frameOptions().sameOrigin()
            .and().csrf().disable()
            .exceptionHandling(n -> n.authenticationEntryPoint(new Http403ForbiddenEntryPoint()))
            .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(@Value("${elimination.client-id}") String clientId, @Value("${elimination.client-secret}") String clientSecret, ObjectMapper mapper) throws IOException {
        return new InMemoryClientRegistrationRepository(
            CommonOAuth2Provider
                .GOOGLE
                .getBuilder("google")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .redirectUri("{baseUrl}/login/oauth2/code/google")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build()
        );
    }

}
