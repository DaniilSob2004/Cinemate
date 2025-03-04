package com.example.cinemate.filter;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.redis.token.AccessTokenRedisStorage;
import com.example.cinemate.service.redis.token.RefreshTokenRedisStorage;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
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

    private final AuthService authService;
    private final JwtTokenService jwtTokenService;
    private final AccessTokenRedisStorage accessTokenRedisStorage;
    private final RefreshTokenRedisStorage refreshTokenRedisStorage;
    private final SendResponseUtil sendResponseUtil;

    public JwtFilter(AuthService authService, JwtTokenService jwtTokenService, AccessTokenRedisStorage accessTokenRedisStorage, RefreshTokenRedisStorage refreshTokenRedisStorage, SendResponseUtil sendResponseUtil) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
        this.accessTokenRedisStorage = accessTokenRedisStorage;
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
        this.sendResponseUtil = sendResponseUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // проверяем, есть ли токен в заголовке и валидный ли он
            jwtTokenService.getValidateTokenFromHeader(request)
                    .ifPresent(token -> {
                        Logger.info("-------- JWT auth filter ({}) --------", token);

                        String requestURI = request.getRequestURI();

                        // проверяем, если токена нет в хранилище
                        if (!accessTokenRedisStorage.isExists(token)) {
                            // если это запрос на update access token (то проверка refresh token)
                            if (requestURI.equals("/api/v1/auth/update-access-token")) {
                                if (!refreshTokenRedisStorage.isExists(token)) {
                                    Logger.error("JWT auth filter - refresh token is incorrect");
                                    this.sendUnauthorizedError(response, "Refresh token is incorrect");
                                    return;
                                }
                            }
                            else {
                                Logger.error("JWT auth filter - access token is incorrect");
                                this.sendUnauthorizedError(response, "Access token is incorrect");
                                return;
                            }
                        }
                        authService.authorizationUserByToken(token);  // авторизация пользователя
                    });
            if (!response.isCommitted()) {
                filterChain.doFilter(request, response);
            }
        } finally {
            // после каждого запроса очищаем контекст, чтобы не запоминался польз. (чтобы отправлять всегда токен)
            SecurityContextHolder.clearContext();
        }
    }

    private void sendUnauthorizedError(HttpServletResponse response, final String message) {
        try {
            sendResponseUtil.sendError(response, new ErrorResponseDto(message, HttpServletResponse.SC_UNAUTHORIZED));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
