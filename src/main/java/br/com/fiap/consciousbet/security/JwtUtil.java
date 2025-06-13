package br.com.fiap.consciousbet.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Chave secreta consistente e suficientemente longa para HS512
    private static final String SECRET = "consciousbet-secret-key-that-is-long-enough-for-hs512-algorithm-security-requirements";

    // Instância única da chave para garantir consistência
    private static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    private static final int TOKEN_VALIDITY = 3600 * 5; // 5 horas

    public String extractEmail(String token) {
        System.out.println("🔧 === EXTRACT EMAIL DEBUG ===");
        System.out.println("🆔 JwtUtil instance ID: " + System.identityHashCode(this));
        System.out.println("🎫 Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");
        System.out.println("🗝️ Using SecretKey: " + secretKey.getAlgorithm());
        System.out.println("🔐 Secret first 20 chars: " + SECRET.substring(0, 20) + "...");

        try {
            String email = extractClaim(token, Claims::getSubject);
            System.out.println("✅ Extracted email: " + email);
            return email;
        } catch (Exception e) {
            System.out.println("❌ Email extraction failed: " + e.getMessage());
            return null;
        }
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Boolean isTokenExpired(String token) {
        System.out.println("🔧 === IS TOKEN EXPIRED DEBUG ===");
        System.out.println("🆔 JwtUtil instance ID: " + System.identityHashCode(this));
        System.out.println("🎫 Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");
        System.out.println("🗝️ Using SecretKey: " + secretKey.getAlgorithm());
        System.out.println("🔐 Secret first 20 chars: " + SECRET.substring(0, 20) + "...");

        try {
            Date expiration = extractExpiration(token);
            boolean isExpired = expiration.before(new Date());
            System.out.println("📅 Token expiration: " + expiration);
            System.out.println("🕒 Current time: " + new Date());
            System.out.println("⏰ Is expired: " + isExpired);
            return isExpired;
        } catch (Exception e) {
            System.out.println("❌ Token expiration check failed: " + e.getMessage());
            System.out.println("❌ Exception type: " + e.getClass().getSimpleName());
            return true; // Se não conseguir verificar, considera expirado
        }
    }

    public String generateToken(String email) {
        System.out.println("🔧 === GENERATE TOKEN DEBUG ===");
        System.out.println("🆔 JwtUtil instance ID: " + System.identityHashCode(this));
        System.out.println("📧 Generating token for email: " + email);
        System.out.println("🗝️ Using SecretKey: " + secretKey.getAlgorithm());
        System.out.println("🔐 Secret first 20 chars: " + SECRET.substring(0, 20) + "...");

        Map<String, Object> claims = new HashMap<>();
        String token = createToken(claims, email);

        System.out.println("🎫 Generated token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");
        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public Boolean validateToken(String token) {
        System.out.println("🔧 === VALIDATE TOKEN DEBUG ===");
        System.out.println("🆔 JwtUtil instance ID: " + System.identityHashCode(this));
        System.out.println("🎫 Token (first 30 chars): " + token.substring(0, Math.min(30, token.length())) + "...");
        System.out.println("🗝️ Using SecretKey: " + secretKey.getAlgorithm());
        System.out.println("🔐 Secret first 20 chars: " + SECRET.substring(0, 20) + "...");

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("✅ Token signature validated successfully");
            System.out.println("📧 Token subject: " + claims.getSubject());
            System.out.println("📅 Token expiration: " + claims.getExpiration());

            boolean isValid = !isTokenExpired(token);
            System.out.println("🔒 Token is valid: " + isValid);
            return isValid;
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            System.out.println("❌ Exception type: " + e.getClass().getSimpleName());
            return false;
        }
    }
}