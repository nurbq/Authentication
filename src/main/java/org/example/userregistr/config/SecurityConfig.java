package org.example.userregistr.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity(debug = true)
@Configuration
//@CrossOrigin
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/auth/register", "/auth/v1/login", "/auth/refreshToken").permitAll()
                                .requestMatchers("/users/all", "/users/admin").hasAuthority("ADMIN")
                                .requestMatchers("/users/manager").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated())
//                .addFilterBefore(new RequestJwtFilter(), AuthorizationFilter.class)
//                .httpBasic(Customizer.withDefaults())
//                .exceptionHandling((exceptionHandling) -> {
//                    exceptionHandling.authenticationEntryPoint((request, response, authException) -> {
//                        response.sendRedirect("/login");
//                    });
//                })
//                .oauth2Login(Customizer.withDefaults())
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

//    @Bean
//    public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
//        OidcUserService oidcUserService = new OidcUserService();
//        return userRequest ->{
//            OidcUser oidcUser = oidcUserService.loadUser(userRequest);
//            Optional.ofNullable(oidcUser.)
//        }
//    }

    @Bean("passwordEncoder")
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
