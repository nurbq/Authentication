package org.example.userregistr.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.List;

public class RobotAuthenticationProvider implements AuthenticationProvider {

    private final List<String> passwords;

    public RobotAuthenticationProvider(List<String> passwords) {
        this.passwords = passwords;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authRequest = (RobotAuthentication) authentication;
        String password = authRequest.getCredentials();

        if (!passwords.contains(password)) {
            throw new BadCredentialsException("You are not Ms Robot");
        }

        return RobotAuthentication.authenticated();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
