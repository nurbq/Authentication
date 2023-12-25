package org.example.userregistr.config.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitAuthenticationProvider implements AuthenticationProvider {
    private final AuthenticationProvider delegate;
    private final Map<String, Instant> authCache = new HashMap<>();

    public RateLimitAuthenticationProvider(AuthenticationProvider delegate) {
        this.delegate = delegate;
    }

    private final Map<String, Instant> cache = new ConcurrentHashMap<>();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var parentAuth = delegate.authenticate(authentication);
        var success = renewCacheEntry(parentAuth);
        if (parentAuth == null) {
            return null;
        }
        if (success) {
            return parentAuth;
        } else {
            throw new BadCredentialsException("Cannot log in right now: rate limited");
        }
    }

    private boolean renewCacheEntry(Authentication authentication) {
        var now = Instant.now();
        var previousLogin = authCache.get(authentication.getName());
        return previousLogin == null || previousLogin.plusSeconds(60).isBefore(now);
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }

}
