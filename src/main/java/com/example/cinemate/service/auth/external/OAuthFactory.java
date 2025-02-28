package com.example.cinemate.service.auth.external;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Централизованная обработка авторизации через внешние провайдеры, которая динамически выбирает нужный сервис
@Service
public class OAuthFactory {

    private final Map<String, OAuthService> authServices;

    public OAuthFactory(List<OAuthService> authServicesList) {
        this.authServices = authServicesList.stream()
                .collect(Collectors.toMap(
                            service -> service.getClass().getSimpleName(),  // название сервиса
                            service -> service  // сам сервис
                        )
                );
    }

    public OAuthService getAuthService(final String provider) {
        return authServices.getOrDefault(
                provider + "AuthService",
                (oauthUser, accessToken) -> { throw new RuntimeException("Unsupported provider: " + provider); }
        );
    }
}
