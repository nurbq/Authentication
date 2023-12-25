package org.example.userregistr.config.security;


import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationEventPublisher publisher) throws Exception {


        {
            http.getSharedObject(AuthenticationManagerBuilder.class).authenticationEventPublisher(publisher);
        }

        var authProvider = new RobotAuthenticationProvider(List.of("beep-boop", "boop-beep"));
        ProviderManager authManager = new ProviderManager(authProvider);
        authManager.setAuthenticationEventPublisher(publisher);
        var configurer = new RobotLoginConfigurer();

        return http
                .authorizeHttpRequests(
                        authorizeConfig -> {
                            authorizeConfig.requestMatchers(HttpMethod.GET, "/users").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                    .requestMatchers(HttpMethod.DELETE, "/users/delete/").hasAuthority("ADMIN")
                                    .requestMatchers("/error").permitAll()
                                    .anyRequest().authenticated();
                        })
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())
                .addFilterBefore(new RobFilter(authManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(new DanielAuthenticationProvider())
                .build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
////        return new InMemoryUserDetailsManager(
////                User.builder()
////                        .username("user")
////                        .password(passwordEncoder().encode("asd"))
////                        .authorities("ROLE_user")
////                        .build()
////        );
//        return customUserDetailsService;
//    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successListener() {
        return event ->
                System.out.printf("\uD83C\uDF1F SUCCESS [%s] %s%n", event.getAuthentication().getClass().getSimpleName(), event.getAuthentication().getName());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

