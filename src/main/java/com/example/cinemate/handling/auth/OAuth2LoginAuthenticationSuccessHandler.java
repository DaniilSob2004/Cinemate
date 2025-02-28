package com.example.cinemate.handling.auth;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.event.StartOAuthEvent;
import com.example.cinemate.exception.auth.*;
import com.example.cinemate.utils.SendResponseUtil;
import com.example.cinemate.utils.StringUtil;
import com.example.cinemate.validate.RegisterValidate;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final RegisterValidate registerValidate;
    private ApplicationEventPublisher eventPublisher;

    public OAuth2LoginAuthenticationSuccessHandler(SendResponseUtil sendResponseUtil, RegisterValidate registerValidate) {
        this.sendResponseUtil = sendResponseUtil;
        this.registerValidate = registerValidate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ErrorResponseDto errorResponse;
        String provider = "-";

        try {
            if (authentication != null) {
                if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
                    Logger.info("-------- OAuth2 Login Authentication Success --------");

                    // получаем данные авторизации google
                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

                    // валидация email (если ошибка, то исключение)
                    registerValidate.validateEmail(oauthUser.getAttribute("email"));

                    provider = StringUtil.capitalizeFirstLetter(oauthToken.getAuthorizedClientRegistrationId());
                    Logger.info("Provider auth: " + provider);

                    // вызываем событие (в publisher отправляется токен)
                    var startOAuthEvent = new StartOAuthEvent(this, oauthUser, provider, response);
                    eventPublisher.publishEvent(startOAuthEvent);
                    if (startOAuthEvent.isResponseHandled()) {  // если ответ был отправлен
                        return;
                    }
                }
            }
            errorResponse = new ErrorResponseDto(provider + " authentication failed", HttpServletResponse.SC_UNAUTHORIZED);

        } catch (UserAlreadyExistsException | OAuthException e) {
            errorResponse = new ErrorResponseDto(e.getMessage(), HttpServletResponse.SC_CONFLICT);
        } catch (InvalidEmailException e) {
            errorResponse = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
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
