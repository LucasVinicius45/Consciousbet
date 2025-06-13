package br.com.fiap.consciousbet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("🔍 Processing request: " + requestURI);

        // Pular autenticação JWT para URLs públicas
        if (isPublicUrl(requestURI)) {
            System.out.println("✅ Public URL, skipping JWT authentication");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("🔐 Auth Header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            System.out.println("🎫 Token extracted (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

            try {
                // Primeiro verifica se o token não está expirado
                if (jwtUtil.isTokenExpired(token)) {
                    System.out.println("❌ Token is expired");
                } else {
                    System.out.println("✅ Token is not expired");
                }

                if (jwtUtil.validateToken(token)) {
                    String email = jwtUtil.extractEmail(token);
                    System.out.println("✅ Token valid for user: " + email);

                    if (email != null) {
                        // Cria autenticação no contexto do Spring Security
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        System.out.println("🔒 Authentication set in Security Context for: " + email);
                    } else {
                        System.out.println("❌ Email extraction returned null");
                    }
                } else {
                    System.out.println("❌ Token validation failed");
                }
            } catch (Exception e) {
                System.out.println("❌ Error processing token: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("❌ No valid Bearer token found");
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicUrl(String requestURI) {
        return requestURI.startsWith("/auth/") ||
                requestURI.startsWith("/h2-console/") ||
                requestURI.startsWith("/swagger-ui/") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resources/") ||
                requestURI.startsWith("/webjars/") ||
                requestURI.equals("/swagger-ui.html") ||
                requestURI.equals("/favicon.ico") ||
                requestURI.equals("/error");
    }
}