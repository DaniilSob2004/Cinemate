package com.example.cinemate.utils;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AppUserConvertDto appUserConvertDto;


    // Генерация токена
    public String generateToken(final AppUserJwtDto appUserJwtDto) {
        Map<String, Object> claims = appUserConvertDto.convertToClaimsJwt(appUserJwtDto);  // получаем данные польз.
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .setSubject(appUserJwtDto.getEmail())
                .setIssuedAt(new Date())
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Получение данных
    public AppUserJwtDto extractAllUserData(final String token) {
        Claims claims = getClaims(token);
        return appUserConvertDto.convertToAppUserJwtDto(claims);  // получаем данные польз. из claims
    }

    public String extractSubject(final String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();  // email
    }

    // Проверка токена
    public boolean validateToken(final String token) {
        try {
            return !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    // Получение токена из заголовка запроса
    public Optional<String> getTokenByAuthHeader(final HttpServletRequest request) {
        // извлекаем заголовок 'Authorization' с префиксом Bearer, и извлекаем токен JWT (если есть) и проверяем его валидность
        String authHeader = request.getHeader(headerAuthName);
        return this.getTokenFromAuthHeaderStr(authHeader);
    }

    public Optional<String> getTokenFromAuthHeaderStr(final String authHeader) {
        if (authHeader != null && authHeader.startsWith(headerValueAuthPrefix)) {
            return Optional.of(authHeader.substring(headerValueAuthPrefix.length() + 1));
        }
        return Optional.empty();
    }

    // Получение даты истечения токена
    public Date getExpirationDateFromToken(final String token) {
        Claims claims = this.getClaims(token);
        return claims.getExpiration();
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
        return this.getClaims(token).getExpiration().before(new Date());
    }
}
