package br.com.fiap.consciousbet.controller;

import br.com.fiap.consciousbet.entity.UserAuth;
import br.com.fiap.consciousbet.repository.UserAuthRepository;
import br.com.fiap.consciousbet.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserAuthRepository repository;
    private final JwtUtil jwtUtil;

    // Use @Autowired para garantir que o Spring injete a mesma instância
    @Autowired
    public AuthController(UserAuthRepository repository, JwtUtil jwtUtil) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        System.out.println("🚀 AuthController initialized with JwtUtil");
        System.out.println("🆔 AuthController JwtUtil instance ID: " + jwtUtil.hashCode());

        // Debug da chave secreta no AuthController - linha removida para corrigir compilação
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserAuth loginData) {
        System.out.println("🔐 Login attempt for email: " + loginData.getEmail());

        // Validação de entrada
        if (loginData.getEmail() == null || loginData.getEmail().trim().isEmpty()) {
            System.out.println("❌ Email is null or empty");
            return ResponseEntity.status(400).body(createErrorResponse("Email is required"));
        }

        if (loginData.getPassword() == null || loginData.getPassword().trim().isEmpty()) {
            System.out.println("❌ Password is null or empty");
            return ResponseEntity.status(400).body(createErrorResponse("Password is required"));
        }

        Optional<UserAuth> user = repository.findByEmail(loginData.getEmail());

        if (user.isEmpty()) {
            System.out.println("❌ User not found: " + loginData.getEmail());
            return ResponseEntity.status(401).body(createErrorResponse("Invalid credentials"));
        }

        System.out.println("✅ User found: " + loginData.getEmail());

        // Aqui você deveria usar BCrypt para comparar senhas hasheadas
        // Por enquanto mantendo a comparação simples
        if (!user.get().getPassword().equals(loginData.getPassword())) {
            System.out.println("❌ Password mismatch for: " + loginData.getEmail());
            return ResponseEntity.status(401).body(createErrorResponse("Invalid credentials"));
        }

        System.out.println("✅ Password correct for: " + loginData.getEmail());

        try {
            String token = jwtUtil.generateToken(user.get().getEmail());
            System.out.println("✅ Token generated successfully");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("email", user.get().getEmail());
            response.put("message", "Login successful");
            response.put("tokenType", "Bearer");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Error generating token: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(createErrorResponse("Internal server error"));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("🔍 Token validation request");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ Invalid Authorization header format");
            return ResponseEntity.status(400).body(createErrorResponse("Invalid Authorization header format"));
        }

        String token = authHeader.substring(7);
        System.out.println("🎫 Token extracted (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");

        try {
            boolean isValid = jwtUtil.validateToken(token);

            Map<String, Object> response = new HashMap<>();
            response.put("valid", isValid);

            if (isValid) {
                String email = jwtUtil.extractEmail(token);
                response.put("email", email);
                response.put("message", "Token is valid");
                System.out.println("✅ Token validation successful for: " + email);
            } else {
                response.put("message", "Token is invalid or expired");
                System.out.println("❌ Token validation failed");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("❌ Error during token validation: " + e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse("Error validating token"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("🔄 Token refresh request");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(createErrorResponse("Invalid Authorization header format"));
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtUtil.extractEmail(token);
            if (email != null) {
                // Gera um novo token
                String newToken = jwtUtil.generateToken(email);

                Map<String, Object> response = new HashMap<>();
                response.put("token", newToken);
                response.put("email", email);
                response.put("message", "Token refreshed successfully");
                response.put("tokenType", "Bearer");

                System.out.println("✅ Token refreshed for: " + email);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body(createErrorResponse("Invalid token"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error refreshing token: " + e.getMessage());
            return ResponseEntity.status(500).body(createErrorResponse("Error refreshing token"));
        }
    }

    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", java.time.Instant.now().toString());
        return error;
    }
}