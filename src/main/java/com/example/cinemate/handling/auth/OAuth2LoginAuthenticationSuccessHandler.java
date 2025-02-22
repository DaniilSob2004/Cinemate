package com.example.cinemate.handling.auth;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.event.StartGoogleAuthEvent;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// Обработчик при успешной авторизации через провайдеры
@Component
public class OAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler, ApplicationEventPublisherAware {

    private final SendResponseUtil sendResponseUtil;
    private ApplicationEventPublisher eventPublisher;

    public OAuth2LoginAuthenticationSuccessHandler(SendResponseUtil sendResponseUtil) {
        this.sendResponseUtil = sendResponseUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ErrorResponseDto errorResponse;

        try {
            if (authentication != null) {
                Logger.info("-------- Google OAuth2 Login Authentication Success --------");

                // получаем данные авторизации google
                OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

                // вызываем событие (в publisher отправляется токен)
                var startGoogleAuthEvent = new StartGoogleAuthEvent(this, oauthUser, response);
                eventPublisher.publishEvent(startGoogleAuthEvent);

                // если ответ был отправлен
                if (startGoogleAuthEvent.isResponseHandled()) {
                    return;
                }
            }
            errorResponse = new ErrorResponseDto("Google authentication failed", HttpServletResponse.SC_UNAUTHORIZED);

        } catch (UserAlreadyExistsException e) {
            errorResponse = new ErrorResponseDto(e.getMessage(), HttpServletResponse.SC_CONFLICT);
        } catch (BadCredentialsException | UserNotFoundException e) {
            errorResponse = new ErrorResponseDto(e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponse = new ErrorResponseDto("Something went wrong", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        // отправляем json ответ с ошибкой
        sendResponseUtil.sendError(response, errorResponse);
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
