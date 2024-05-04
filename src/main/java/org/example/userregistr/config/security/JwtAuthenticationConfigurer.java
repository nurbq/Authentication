package org.example.userregistr.config.security;

import jakarta.servlet.http.HttpServletResponse;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.Builder;
import org.example.userregistr.config.security.filters.JWTLogoutFilter;
import org.example.userregistr.config.security.filters.RefreshTokenFilter;
import org.example.userregistr.config.security.filters.RequestJwtTokensFilter;
import org.example.userregistr.config.security.jwt.JwtAuthenticationConverter;
import org.example.userregistr.config.security.jwt.TokenAuthenticationUserDetailsService;
import org.example.userregistr.model.jwt.Token;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Builder(access = AccessLevel.PUBLIC)
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> refreshTokenStringSerializer;
    private Function<Token, String> accessTokenStringSerializer;
    private Function<String, Token> refreshTokenStringDeserializer;
    private Function<String, Token> accessTokenStringDeserializer;
    @Qualifier("primaryJdbcTemplate")
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void init(HttpSecurity builder) throws Exception {
        CsrfConfigurer csrfConfigurer = builder.getConfigurer(CsrfConfigurer.class);
        if (csrfConfigurer != null) {
            csrfConfigurer.ignoringRequestMatchers(new AntPathRequestMatcher("/jwt/tokens", HttpMethod.POST.name()));
        }
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        RequestJwtTokensFilter jwtTokensFilter = new RequestJwtTokensFilter();
        jwtTokensFilter.setAccessTokenStringSerializer(this.accessTokenStringSerializer);
        jwtTokensFilter.setRefreshTokenStringSerializer(this.refreshTokenStringSerializer);

        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(accessTokenStringDeserializer, refreshTokenStringDeserializer));

        jwtAuthenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        jwtAuthenticationFilter.setFailureHandler(((request, response, exception) -> response.sendError(HttpServletResponse.SC_FORBIDDEN)));


        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthenticationUserDetailsService(jdbcTemplate));

        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenStringSerializer(accessTokenStringSerializer);

        var jwtLogoutFilter = new JWTLogoutFilter(jdbcTemplate);

        builder.addFilterAfter(jwtTokensFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(authenticationProvider);
    }
}
