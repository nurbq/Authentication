package org.example.userregistr.config.csrf;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.auth.Token;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class TokenCookieAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> tokenCookieStringDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return (Authentication) Stream.of(Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("__Host-auth-token"))
                    .findFirst()
                    .map(cookie -> {
                        var token = this.tokenCookieStringDeserializer.apply(cookie.getValue());
                        return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
                    }).orElse(null));
        }
        return null;
    }
}
