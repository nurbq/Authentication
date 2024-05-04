package org.example.userregistr.config.security;

import java.util.function.Function;
import lombok.Builder;
import org.example.userregistr.config.security.filters.RequestJwtTokensFilter;
import org.example.userregistr.model.jwt.Token;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Builder
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {

    private Function<Token, String> refreshTokenStringSerializer;
    private Function<Token, String> accessTokenStringSerializer;

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


        builder.addFilterAfter(jwtTokensFilter, ExceptionTranslationFilter.class);
    }
}
