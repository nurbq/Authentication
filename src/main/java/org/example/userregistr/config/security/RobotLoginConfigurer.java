package org.example.userregistr.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

public class RobotLoginConfigurer extends AbstractHttpConfigurer<RobotLoginConfigurer, HttpSecurity> {

    private List<String> passwords = new ArrayList<>();

    @Override
    public void init(HttpSecurity http) throws Exception {
        super.init(http);
        // step 1
        // initializes a bunch of objects
        // -> AuthenticationProviders
        http.authenticationProvider(
                new RobotAuthenticationProvider(passwords));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        // step 2
        // this also initializes objects, but can reuse objects from step 1, even from other configurers
        // -> Filters
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        http.addFilterBefore(new RobFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);
    }

    public RobotLoginConfigurer password(String password) {
        this.passwords.add(password);
        return this;
    }
}
