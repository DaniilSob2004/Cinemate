package com.example.cinemate.utils;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Component
public class JwtTokenUtil {

    @Value("${security.secret_key}")
    private String secretKey;

    @Value("${security.expiration_time}")
    private Long expirationTime;

    @Value("${security.header_auth_name}")
    private String headerAuthName;

    @Value("${security.header_value_auth_prefix}")
    private String headerValueAuthPrefix;

    // Генерация токена
    public String generateToken(final String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken2(final AppUserJwtDto appUserJwtDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", appUserJwtDto.getUsername());
        claims.put("firstname", appUserJwtDto.getFirstname());
        claims.put("surname", appUserJwtDto.getSurname());
        claims.put("roles", appUserJwtDto.getRoles());
        claims.put("phoneNum", appUserJwtDto.getPhoneNum());
        claims.put("avatar", appUserJwtDto.getAvatar());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setSubject(appUserJwtDto.getEmail())
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Получение имени пользователя из токена
    public String extractEmail(final String token) {
        return getClaims(token).getSubject();
    }

    public AppUserJwtDto extractAllUserData(final String token) {
        Claims claims = getClaims(token);

        Object claimRoles = claims.get("roles");
        List<String> roles = claimRoles != null ? (List<String>) claimRoles : new ArrayList<>();

        return new AppUserJwtDto(
                claims.get("username", String.class),
                claims.get("firstname", String.class),
                claims.get("surname", String.class),
                roles,
                claims.getSubject(),  // email
                claims.get("phoneNum", String.class),
                claims.get("avatar", String.class)
        );
    }

    // Проверка токена
    public boolean validateToken(final String token) {
        try {
            var passClaims = getClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    // Получение токена из заголовка запроса
    public Optional<String> getTokenByAuthHeader(final HttpServletRequest request) {
        // извлекаем заголовок 'Authorization' с префиксом Bearer, и извлекаем токен JWT (если есть) и проверяем его валидность
        String authHeader = request.getHeader(headerAuthName);
        if (authHeader != null && authHeader.startsWith(headerValueAuthPrefix)) {
            return Optional.of(authHeader.substring(headerValueAuthPrefix.length() + 1));
        }
        return Optional.empty();
    }


    // Получение ключа
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // Извлечение всех claims
    private Claims getClaims(final String token) {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseSignedClaims(token);

        return jws.getPayload();
    }

    // Проверка истечения срока токена
    private boolean isTokenExpired(final String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
