package com.example.cinemate.listener;

import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.event.StartOAuthEvent;
import com.example.cinemate.service.auth.external.BaseOAuthService;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import java.io.IOException;

@Component
public class OAuthListener {

    private final BaseOAuthService baseOAuthService;
    private final SendResponseUtil sendResponseUtil;

    public OAuthListener(BaseOAuthService baseOAuthService, SendResponseUtil sendResponseUtil) {
        this.baseOAuthService = baseOAuthService;
        this.sendResponseUtil = sendResponseUtil;
    }

    @EventListener
    public void handleOAuthEvent(@NonNull final StartOAuthEvent event) {
        OAuth2User oauthUser = event.getOauthUser();  // получаем данные авторизации google
        if (oauthUser != null) {
            // вход/регистрация
            String token = baseOAuthService.processOAuth(event.getProvider(), oauthUser);

            // отправляем json ответ с токеном
            this.sendAuthResponse(event, token);
        }
    }

    private void sendAuthResponse(final StartOAuthEvent event, final String token) {
        try {
            Logger.info("Token - " + token);
            sendResponseUtil.sendData(event.getResponse(), new AuthResponseDto(token));
            event.setResponseHandled(true);  // флаг ответ отправлен
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
