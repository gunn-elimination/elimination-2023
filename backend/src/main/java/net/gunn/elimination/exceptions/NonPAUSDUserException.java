package net.gunn.elimination.exceptions;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;

public class NonPAUSDUserException extends OAuth2AuthenticationException {
    public NonPAUSDUserException(String email) {
        super(new OAuth2Error(OAuth2ErrorCodes.ACCESS_DENIED), email + " is not a PAUSD email.  Make sure you're logged in with your school account.");
    }
}
