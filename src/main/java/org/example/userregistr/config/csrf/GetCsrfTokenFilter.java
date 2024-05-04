package org.example.userregistr.config.csrf;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@Setter
public class GetCsrfTokenFilter extends OncePerRequestFilter {

    private final CsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestMatcher requestMatcher = new AntPathRequestMatcher("/csrf", HttpMethod.GET.name());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(APPLICATION_JSON_VALUE);
            this.objectMapper.writeValue(response.getWriter(), this.csrfTokenRepository.loadDeferredToken(request, response).get());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
