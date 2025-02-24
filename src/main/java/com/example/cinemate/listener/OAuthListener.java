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
        OAuth2User user = event.getOauthUser();  // получаем данные авторизации google
        if (user != null) {
            // вход/регистрация
            String token = baseOAuthService.processOAuth(event.getProvider(), user);
            AuthResponseDto authResponseDto = new AuthResponseDto(token);

            Logger.info("Token - " + authResponseDto.getToken());

            // отправляем json ответ с токеном
            try {
                sendResponseUtil.sendData(event.getResponse(), authResponseDto);
                event.setResponseHandled(true);  // флаг ответ отправлен
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
