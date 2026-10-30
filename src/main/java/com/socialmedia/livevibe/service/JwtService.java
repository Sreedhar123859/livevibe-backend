package com.socialmedia.livevibe.service;

import com.socialmedia.livevibe.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret must not be empty");
        }

        if (expirationMs <= 0) {
            throw new IllegalArgumentException(
                    "JWT expiration must be positive");
        }

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.expirationMs = expirationMs;
//        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        this.expirationMs = expirationMs;
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(user.getId())
                .claim("name", user.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMs)))
                .signWith(signingKey)
                .compact();
    }

    public String extractUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, User user) {
        Claims claims = parseClaims(token);
        return claims.getSubject() != null
                && claims.getSubject().equals(user.getId())
                && claims.getExpiration() != null
                && claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
