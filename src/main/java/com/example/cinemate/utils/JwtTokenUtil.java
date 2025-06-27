package com.example.cinemate.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    private final SecretKey jwtSecretKey;

    public JwtTokenUtil(@Value("${security.secret_key}") String secretKey) {
        this.jwtSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));  // получение ключа
    }

    // Генерация токена
    public String generateToken(final Map<String, Object> claims, final String subject, final long expirationTimeMin) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMin * 60 * 1000))
                .setSubject(subject)  // id
                .setIssuedAt(new Date())
                .signWith(this.jwtSecretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Проверка токена
    public boolean validateToken(final String token) {
        if (token == null || token.isEmpty()) {
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

    // Извлечение всех claims
    public Claims getClaims(final String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(this.jwtSecretKey)
                .build()
                .parseSignedClaims(token);
        return jws.getPayload();
    }

    public Claims getClaimsFromExpiredToken(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(this.jwtSecretKey)
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

    // Проверка истечения срока токена
    private boolean isTokenExpired(final String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
