package org.example.userregistr.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class RobFilter extends OncePerRequestFilter {

    public final String HEADER_NAME = "x-robot-password";

    private final AuthenticationManager authenticationManager;

    public RobFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //0. Should we execute filter?
        if (!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. Authentication Decision
        String password = request.getHeader(HEADER_NAME);
        RobotAuthentication authRequest = RobotAuthentication.unauthenticated(password);

        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);
            SecurityContext newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request, response);
            return;
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-type", "test/plain;charset=utf-8");
            response.getWriter().println(e.getMessage());
            return;
        }
        // 2. Do the Rest
    }
}
