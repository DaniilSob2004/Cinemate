package com.example.cinemate.listener;

import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.event.StartGoogleAuthEvent;
import com.example.cinemate.service.auth.GoogleAuthService;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import java.io.IOException;

@Component
public class GoogleAuthListener {

    private final GoogleAuthService googleAuthService;
    private final SendResponseUtil sendResponseUtil;

    public GoogleAuthListener(GoogleAuthService googleAuthService, SendResponseUtil sendResponseUtil) {
        this.googleAuthService = googleAuthService;
        this.sendResponseUtil = sendResponseUtil;
    }

    @EventListener
    public void handleGoogleAuthEvent(@NonNull final StartGoogleAuthEvent event) {
        OAuth2User user = event.getOauthUser();  // получаем данные авторизации google
        if (user != null) {
            // вход/регистрация
            String token = googleAuthService.processGoogleAuth(user);
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
