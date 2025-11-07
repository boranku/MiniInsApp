package com.myapp.app.security;

import com.myapp.app.entity.Token;
import com.myapp.app.repository.TokenRepository;
import com.myapp.app.util.RequestContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();
        if (isPublicEndpoint(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Missing Authorization header\"}");
                return;
            }

            String tokenValue = header.substring(7);
            Token token = tokenRepository.findByValueAndActiveTrue(tokenValue).orElse(null);

            if (token == null || token.getExpiresAt().isBefore(OffsetDateTime.now())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\":\"Invalid or expired token\"}");
                return;
            }

            RequestContext.setUser(token.getUser());
            filterChain.doFilter(request, response);

        } finally {
            RequestContext.clear();
        }
    }

    private boolean isPublicEndpoint(String path, String method) {
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/signup")) {
            return true;
        }
        // Logout endpoint - POST /api/auth/logout
        if (path.equals("/api/auth/logout") && "POST".equals(method)) {
            return true;
        }

        return false;
    }
}