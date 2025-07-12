package com.example.cinemate.listener;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.event.StartOAuthEvent;
import com.example.cinemate.service.auth.external.OAuthFactory;
import com.example.cinemate.service.auth.external.OAuthService;
import com.example.cinemate.service.redis.AuthProviderTokenStorage;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuthListener {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final AuthProviderTokenStorage authProviderTokenStorage;
    private final OAuthFactory authFactory;

    public OAuthListener(AuthProviderTokenStorage authProviderTokenStorage, OAuthFactory authFactory) {
        this.authProviderTokenStorage = authProviderTokenStorage;
        this.authFactory = authFactory;
    }

    @EventListener
    public void handleOAuthEvent(@NonNull final StartOAuthEvent event) throws IOException {
        OAuth2User oauthUser = event.getOauthUser();  // данные авторизации google
        if (oauthUser != null) {
            OAuthService authService = authFactory.getAuthService(event.getProvider());  // получение сервиса по названию провайдера
            var authResponseDto = authService.processAuth(oauthUser, event.getAccessToken());  // запуск авторизации

            // временно сохраняем токены
            String stateId = UUID.randomUUID().toString();
            authProviderTokenStorage.addToStorage(stateId, authResponseDto);

            Logger.info("State id for oauth: " + stateId);

            // редиректим на фронт с stateId
            event.getResponse().sendRedirect(frontendUrl + Endpoint.OAUTH_SUCCESS_TOKENS + stateId);
            event.setResponseHandled(true);  // флаг ответ отправлен
        }
    }
}
