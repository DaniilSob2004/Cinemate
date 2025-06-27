package com.example.cinemate.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BaseAuthUtilTest {

    private BaseAuthUtil baseAuthUtil;

    @BeforeEach
    void setUp() throws Exception {
        baseAuthUtil = new BaseAuthUtil();

        Field headerName = BaseAuthUtil.class.getDeclaredField("headerAuthName");
        headerName.setAccessible(true);
        headerName.set(baseAuthUtil, "Authorization");

        Field prefix = BaseAuthUtil.class.getDeclaredField("headerBasicAuthPrefix");
        prefix.setAccessible(true);
        prefix.set(baseAuthUtil, "Basic");
    }

    @Test
    void getCredentialsFromHeader_ShouldReturnDecodedCredentials() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        String credentials = "user123:pass456";
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        when(request.getHeader("Authorization")).thenReturn("Basic " + encoded);

        Optional<String> result = baseAuthUtil.getCredentialsFromHeader(request);
        assertTrue(result.isPresent());
        assertEquals(credentials, result.get());
    }

    @Test
    void getCredentialsFromHeader_ShouldReturnEmptyIfHeaderMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        Optional<String> result = baseAuthUtil.getCredentialsFromHeader(request);
        assertTrue(result.isEmpty());
    }

    @Test
    void getCredentialsFromHeader_ShouldReturnEmptyIfHeaderWrongPrefix() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");

        Optional<String> result = baseAuthUtil.getCredentialsFromHeader(request);
        assertTrue(result.isEmpty());
    }

    @Test
    void getLoginPassword_ShouldReturnLoginAndPassword() {
        Optional<String[]> result = baseAuthUtil.getLoginPassword("admin:secret");

        assertTrue(result.isPresent());
        assertArrayEquals(new String[]{"admin", "secret"}, result.get());
    }

    @Test
    void getLoginPassword_ShouldReturnEmptyIfNoColon() {
        Optional<String[]> result = baseAuthUtil.getLoginPassword("invalidStringWithoutColon");

        assertTrue(result.isEmpty());
    }
}