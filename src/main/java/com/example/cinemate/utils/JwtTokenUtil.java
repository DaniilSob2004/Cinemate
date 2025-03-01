package com.example.cinemate.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    @Value("${security.secret_key}")
    private String secretKey;

    // Генерация токена
    public String generateToken(final Map<String, Object> claims, final String subject, final long expirationTimeMin) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMin * 60 * 1000))
                .setSubject(subject)  // id
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Проверка токена
    public boolean validateToken(final String token) {
        if (token.isEmpty()) {
            return false;
        }
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean validateTokenWithoutExpiration(final String token) {
        if (token.isEmpty()) {
            return false;
        }
        try {
            getClaimsFromExpiredToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Извлечение sub
    public String getSubject(final String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Извлечение всех claims
    public Claims getClaims(final String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }

    public Claims getClaimsFromExpiredToken(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // Получение даты истечения токена
    public Date getExpirationDateFromToken(final String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration();
    }

    // Получение ключа
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Проверка истечения срока токена
    private boolean isTokenExpired(final String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
