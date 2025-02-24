package com.example.cinemate.service.auth.external;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class BaseOAuthService {

    private final OAuthFactory authFactory;

    public BaseOAuthService(OAuthFactory authFactory) {
        this.authFactory = authFactory;
    }

    public String processOAuth(String provider, OAuth2User oauthUser) {
        OAuthService authService = authFactory.getAuthService(provider);  // получение сервиса по названию провайдера
        return authService.processAuth(oauthUser);  // запуск авторизации
    }
}
