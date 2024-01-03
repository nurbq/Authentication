package org.example.userregistr.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

public class RobotAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1.decide
        if (!Collections.list(request.getHeaderNames()).contains("x-robot-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Authenticate
        if (!Objects.equals(request.getHeader("x-robot-password"), "beep-boop")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType("text/plain;charset=utf-8");
            response.getWriter().write("❌ You r not ms Robot!!!");
            return;
        }
        RobotAuthenticationToken robotAuthenticationToken = new RobotAuthenticationToken();
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(robotAuthenticationToken);
        SecurityContextHolder.setContext(newContext);

        // 3. next
        filterChain.doFilter(request, response);

        // no cleanup
    }
}
