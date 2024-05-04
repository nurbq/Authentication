package org.example.userregistr.config.security.jwt.fabrics;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.userregistr.model.jwt.Token;

@Setter
@Slf4j
public class DefaultAccessTokenFactory implements Function<Token, Token> {

    private Duration tokenTtl = Duration.ofMinutes(5);

    @Override
    public Token apply(Token token) {
        var tokenAuthorities = token.roles().stream()
                .filter(authority -> authority.startsWith("GRANT_"))
                .map(authority -> authority.replace("GRANT_", ""))
                .toList();
        var now = Instant.now();

        return new Token(
                token.id(),
                token.subject(),
                tokenAuthorities,
                now,
                now.plus(this.tokenTtl)
        );
    }
}
