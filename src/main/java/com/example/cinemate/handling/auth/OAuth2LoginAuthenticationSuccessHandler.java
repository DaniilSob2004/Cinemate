package com.example.cinemate.handling.auth;

import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.service.auth.GoogleAuthService;
import com.example.cinemate.utils.SendResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private GoogleAuthService googleAuthService;

    @Autowired
    private SendResponseUtil sendResponseUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        ErrorResponseDto errorResponse;

        try {
            if (authentication != null) {
                Logger.info("-------- Google OAuth2 Login Authentication Success --------");

                // получаем данные авторизации google и вход/регистрация
                OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                String token = googleAuthService.processGoogleAuth(oauthUser);
                AuthResponseDto authResponseDto = new AuthResponseDto(token);

                Logger.info("Token - " + authResponseDto.getToken());

                // отправляем json ответ с токеном
                sendResponseUtil.sendData(response, authResponseDto);

                return;
            }
            else {
                errorResponse = new ErrorResponseDto("Google authentication failed", HttpServletResponse.SC_UNAUTHORIZED);
            }
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

    /*private void sendToken(final HttpServletResponse response, final String token) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"token\": \"" + token + "\"}");
        response.getWriter().flush();
    }*/
}
