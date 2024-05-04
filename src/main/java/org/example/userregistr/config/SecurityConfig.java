package org.example.userregistr.config;


import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import java.text.ParseException;
import lombok.RequiredArgsConstructor;
import org.example.userregistr.config.csrf.GetCsrfTokenFilter;
import org.example.userregistr.config.csrf.TokenCookieAuthenticationConfigurer;
import org.example.userregistr.config.csrf.TokenCookieJweStringSerializer;
import org.example.userregistr.config.csrf.TokenCookieSessionAuthenticationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@EnableWebSecurity(debug = true)
@Configuration
//@CrossOrigin
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer() {
        return new TokenCookieAuthenticationConfigurer(null, null);
    }

    @Bean
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
            @Value("${jwt.cookie-token-key}") String cookieTokenKey
    ) throws ParseException, KeyLengthException {
        return new TokenCookieJweStringSerializer(
                new DirectEncrypter(OctetSequenceKey.parse(cookieTokenKey)
        ));
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, TokenCookieJweStringSerializer tokenCookieJweStringSerializer) throws Exception {
        var tokenCookieSessionAuthenticationStrategy = new TokenCookieSessionAuthenticationStrategy();
        tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);

        http
                .httpBasic(Customizer.withDefaults())
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .authorizeHttpRequests(request ->
                        request
                                .requestMatchers("/manager").hasRole("MANAGER")
                                .requestMatchers("/error", "index.html").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(tokenCookieSessionAuthenticationStrategy))
                .csrf(csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy(((authentication, request, response) -> {
                        })));
        http.with(tokenCookieAuthenticationConfigurer(), Customizer.withDefaults());
        return http.build();
    }

    @Bean(name = "passwordEncoder")
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
