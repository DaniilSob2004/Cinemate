package com.example.cinemate.utils;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    void setUp() {
        // пример секрета (должен быть минимум 256 бит → 32 байта → Base64 = 44+ символов)
        byte[] keyBytes = "12345678901234567890123456789012".getBytes();
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);
        jwtTokenUtil = new JwtTokenUtil(secretKey);
    }

    @Test
    void generateToken_ShouldBeValid() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "user");

        String token = jwtTokenUtil.generateToken(claims, "userId123", 10);

        assertNotNull(token);
        assertTrue(jwtTokenUtil.validateToken(token));

        Claims parsed = jwtTokenUtil.getClaims(token);
        assertEquals("userId123", parsed.getSubject());
        assertEquals("user", parsed.get("role"));
    }

    @Test
    void validateToken_ShouldReturnFalseForEmptyOrNull() {
        assertFalse(jwtTokenUtil.validateToken(null));
        assertFalse(jwtTokenUtil.validateToken(""));
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        String token = jwtTokenUtil.generateToken(new HashMap<>(), "expiredUser", 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        assertFalse(jwtTokenUtil.validateToken(token));
    }

    @Test
    void validateTokenWithoutExpiration_ShouldWorkOnExpiredToken() {
        String token = jwtTokenUtil.generateToken(new HashMap<>(), "expiredUser", 0);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        assertTrue(jwtTokenUtil.validateTokenWithoutExpiration(token));
    }

    @Test
    void getClaims_ShouldReturnCorrectClaims() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", "test@example.com");

        String token = jwtTokenUtil.generateToken(claims, "abc", 5);

        Claims parsed = jwtTokenUtil.getClaims(token);
        assertEquals("test@example.com", parsed.get("email"));
        assertEquals("abc", parsed.getSubject());
    }

    @Test
    void getExpirationDateFromToken_ShouldReturnCorrectExpiration() {
        String token = jwtTokenUtil.generateToken(new HashMap<>(), "user", 5);
        Date expiration = jwtTokenUtil.getExpirationDateFromToken(token);

        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void getClaimsFromExpiredToken_ShouldStillReturnClaims() {
        String token = jwtTokenUtil.generateToken(new HashMap<>(), "expired", 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}

        Claims claims = jwtTokenUtil.getClaimsFromExpiredToken(token);
        assertEquals("expired", claims.getSubject());
    }
}