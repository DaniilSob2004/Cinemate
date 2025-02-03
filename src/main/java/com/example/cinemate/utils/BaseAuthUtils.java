package com.example.cinemate.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

@Component
public class BaseAuthUtils {

    @Value("${security.header_auth_name}")
    private String headerAuthName;

    @Value("${security.header_value_basic_auth_prefix}")
    private String headerBasicAuthPrefix;

    public Optional<String> getCredentialsFromHeader(final HttpServletRequest request) {
        // извлекаем заголовок 'Authorization' с префиксом Basic
        String authHeader = request.getHeader(headerAuthName);
        if (authHeader == null || !authHeader.startsWith(headerBasicAuthPrefix)) {
            return Optional.empty();
        }

        // декодируем заголовок Basic Auth
        String decodedCredentials = decodeCredentials(authHeader);
        return Optional.of(decodedCredentials);
    }

    public Optional<String[]> getLoginPassword(String credentialsStr) {
        String[] credentials = credentialsStr.split(":", 2);
        if (credentials.length != 2) {
            return Optional.empty();
        }
        return Optional.of(credentials);
    }

    private String decodeCredentials(final String authHeader) {
        String base64Credentials = authHeader.substring(headerBasicAuthPrefix.length() + 1);
        byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
