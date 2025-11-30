package com.sonchasapps.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @PostConstruct
    public void init() {
        System.out.println("=== JwtService INITIALIZED ===");
        System.out.println("Secret key length: " + (SECRET_KEY != null ? SECRET_KEY.length() : "NULL"));
    }

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
            System.out.println("✅ Secret key decoded successfully, bytes length: " + keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            System.out.println("❌ ERROR decoding secret key: " + e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            System.out.println("✅ Token is valid");
            return true;
        } catch (JwtException e) {
            System.out.println("❌ Token validation failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Token validation failed: IllegalArgumentException - " + e.getMessage());
            return false;
        }
    }

    public String extractUserId(String token) {
        try {
            String userId = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            System.out.println("✅ Extracted userId: " + userId);
            return userId;
        } catch (JwtException e) {
            System.out.println("❌ Failed to extract userId: " + e.getMessage());
            return null;
        }
    }
}