package com.example.cinemate.handling.auth;

import com.example.cinemate.service.auth.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        Logger.info("-------- Google OAuth2 Login Authentication Success --------");

        try {
            if (authentication != null) {
                // получаем данные авторизации google и вход/регистрация
                OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                String token = googleAuthService.processGoogleAuth(oauthUser);
                Logger.info("Token: " + token);

                // отправляем JSON-ответ с токеном
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"token\": \"" + token + "\"}");
                response.getWriter().flush();
            }
            else {
                Logger.error("User authentication google data is NULL");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Google authentication failed");
            }
        } catch (Exception e) {
            Logger.error("Google authentication error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}
