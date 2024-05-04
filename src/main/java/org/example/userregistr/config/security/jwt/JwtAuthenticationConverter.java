package org.example.userregistr.config.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.jwt.Token;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> accessTokenStringDeserializer;
    private final Function<String, Token> refreshTokenStringDeserializer;

    @Override
    public Authentication convert(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            Token accesstoken = accessTokenStringDeserializer.apply(token);
            if (accesstoken != null) {
                return new PreAuthenticatedAuthenticationToken(
                        accesstoken,
                        token
                );
            }
            var refreshToken = refreshTokenStringDeserializer.apply(token);
            if (refreshToken != null) {
                return new PreAuthenticatedAuthenticationToken(
                        refreshToken,
                        token
                );
            }
        }


        return null;
    }
}
