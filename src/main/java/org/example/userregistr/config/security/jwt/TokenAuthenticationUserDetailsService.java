package org.example.userregistr.config.security.jwt;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.model.jwt.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            Boolean isExists = jdbcTemplate.queryForObject("""
                    select exists(select id from t_deactivated_token where id = ?)
                    """, Boolean.class, token.id());
            return new TokenUser(
                    token.subject(),
                    "nopassword",
                    true,
                    true,
                    !Boolean.TRUE.equals(isExists) &&
                    token.expiresAt().isAfter(Instant.now()),
                    true,
                    token.roles().stream()
                            .map(SimpleGrantedAuthority::new).toList(),
                    token
            );
        }
        throw new UsernameNotFoundException(authenticationToken.getPrincipal().toString());
    }
}
