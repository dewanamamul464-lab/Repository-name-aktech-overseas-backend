package com.aktech.overseas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET =
            System.getenv("JWT_SECRET");

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET.getBytes(StandardCharsets.UTF_8)
            );

    // Generate JWT Token
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60
                        )
                )
                .signWith(key)
                .compact();
    }

    // Read Claims
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extract Username
    public String extractUsername(String token) {

        return getClaims(token).getSubject();
    }

    // Extract Role
    public String extractRole(String token) {

        return getClaims(token)
                .get("role", String.class);
    }

    // Validate Token
    public boolean isTokenValid(
            String token,
            String username) {

        return extractUsername(token).equals(username)
                && getClaims(token)
                        .getExpiration()
                        .after(new Date());
    }
}