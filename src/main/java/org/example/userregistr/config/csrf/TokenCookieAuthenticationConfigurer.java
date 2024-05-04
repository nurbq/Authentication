package org.example.userregistr.config.csrf;

import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.userregistr.config.CustomUserDetailsService;
import org.example.userregistr.dao.repository.RoleRepository;
import org.example.userregistr.dao.repository.UserRepository;
import org.example.userregistr.model.auth.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.csrf.CsrfFilter;

@RequiredArgsConstructor
@Setter
public class TokenCookieAuthenticationConfigurer extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {

    private Function<String, Token> tokenCookieStringDeserializer;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public void init(HttpSecurity builder) throws Exception {

    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationFilter cookieAuthenticationFilter = new AuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                new TokenCookieAuthenticationConverter(this.tokenCookieStringDeserializer));
        cookieAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {
        });
        cookieAuthenticationFilter.setFailureHandler((
                        new AuthenticationEntryPointFailureHandler(
                                new Http403ForbiddenEntryPoint())
                )
        );

        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService((AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken>) new CustomUserDetailsService(
                userRepository,
                roleRepository
        ));

        builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class);
    }
}
