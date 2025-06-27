package com.example.cinemate.service.auth.jwt;

import com.example.cinemate.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
public class JwtTokenService {

    @Value("${security.header_auth_name}")
    private String headerAuthName;

    @Value("${security.header_value_auth_prefix}")
    private String headerValueAuthPrefix;

    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenService(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public String generateToken(final Map<String, Object> claims, final String subject, final long expirationTimeMin) {
        return jwtTokenUtil.generateToken(claims, subject, expirationTimeMin);
    }

    public Claims getClaims(final String token) {
        return jwtTokenUtil.getClaims(token);
    }

    public Claims getClaimsFromExpired(final String token) {
        return jwtTokenUtil.getClaimsFromExpiredToken(token);
    }

    public boolean validateTokenWithoutExpiration(final String token) {
        return jwtTokenUtil.validateTokenWithoutExpiration(token);
    }

    public Optional<String> getValidateTokenFromHeader(final HttpServletRequest request) {
        return this.getTokenByAuthHeader(request).filter(jwtTokenUtil::validateToken);
    }

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

    public Date getExpirationDateFromToken(final String token) {
        return jwtTokenUtil.getExpirationDateFromToken(token);
    }
}
