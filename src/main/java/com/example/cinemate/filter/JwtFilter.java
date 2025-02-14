package com.example.cinemate.filter;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.auth.jwtblacklist.JwtBlacklistService;
import com.example.cinemate.utils.SendErrorResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.tinylog.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// фильтрация HTTP-запросов и проверка JWT-токена
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtBlacklistService jwtBlacklistService;

    @Autowired
    private SendErrorResponseUtil sendErrorResponseUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // проверяем, есть ли токен в заголовке, валидный ли он
            String token = authService.tokenValidateFromHeader(request).orElse(null);

            Logger.info("JWT auth filter header token: " + token);

            // проверяем, есть ли токен в blacklist
            if (token != null) {
                if (jwtBlacklistService.isBlacklisted(token)) {
                    Logger.error("JWT auth filter - token is blacklisted");

                    var errorResponseDto = new ErrorResponseDto("Token is blacklisted", HttpServletResponse.SC_UNAUTHORIZED);
                    sendErrorResponseUtil.sendError(response, errorResponseDto);

                    return;
                }
                else {
                    authService.authorizationUserByToken(token);  // авторизовываем пользователя
                }
            }

            chain.doFilter(request, response);
        } finally {
            // после каждого запроса очищаем контекст, чтобы не запоминался польз. (чтобы вводить всегда токен)
            SecurityContextHolder.clearContext();
        }
    }
}
