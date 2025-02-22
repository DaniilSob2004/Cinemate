package com.example.cinemate.listener;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.event.JwtFilterEvent;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.redis.BlacklistTokenRedisService;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilterListener {

    private final AuthService authService;
    private final BlacklistTokenRedisService blacklistTokenRedisService;
    private final SendResponseUtil sendResponseUtil;

    public JwtFilterListener(AuthService authService, BlacklistTokenRedisService blacklistTokenRedisService, SendResponseUtil sendResponseUtil) {
        this.authService = authService;
        this.blacklistTokenRedisService = blacklistTokenRedisService;
        this.sendResponseUtil = sendResponseUtil;
    }

    @EventListener
    public void handleAuthorizationByTokenEvent(@NonNull final JwtFilterEvent event) {
        // проверяем, есть ли токен в заголовке и валидный ли он
        authService.tokenValidateFromHeader(event.getRequest())
                .ifPresent(token -> {
                    Logger.info("-------- JWT auth filter ({}) --------", token);

                    // проверяем, есть ли токен в blacklist
                    if (blacklistTokenRedisService.isBlacklisted(token)) {
                        Logger.error("JWT auth filter - token is blacklisted");
                        try {
                            sendResponseUtil.sendError(
                                    event.getResponse(),
                                    new ErrorResponseDto("Token is blacklisted", HttpServletResponse.SC_UNAUTHORIZED)
                            );
                            event.setResponseHandled(true);  // флаг ответ отправлен
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }

                    // авторизация пользователя
                    authService.authorizationUserByToken(token);
                });
    }
}
