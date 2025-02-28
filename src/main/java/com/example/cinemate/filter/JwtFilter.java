package com.example.cinemate.filter;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.redis.BlacklistTokenRedisService;
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
public class JwtFilter extends OncePerRequestFilter{

    private final AuthService authService;
    private final BlacklistTokenRedisService blacklistTokenRedisService;
    private final SendResponseUtil sendResponseUtil;

    public JwtFilter(AuthService authService, BlacklistTokenRedisService blacklistTokenRedisService, SendResponseUtil sendResponseUtil) {
        this.authService = authService;
        this.blacklistTokenRedisService = blacklistTokenRedisService;
        this.sendResponseUtil = sendResponseUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            // проверяем, есть ли токен в заголовке и валидный ли он
            authService.tokenValidateFromHeader(request)
                    .ifPresent(token -> {
                        Logger.info("-------- JWT auth filter ({}) --------", token);

                        // проверяем, есть ли токен в blacklist
                        if (blacklistTokenRedisService.isBlacklisted(token)) {
                            Logger.error("JWT auth filter - token is blacklisted");
                            try {
                                sendResponseUtil.sendError(
                                        response,
                                        new ErrorResponseDto("Token is blacklisted", HttpServletResponse.SC_UNAUTHORIZED)
                                );
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            return;
                        }
                        authService.authorizationUserByToken(token);  // авторизация пользователя
                    });
            chain.doFilter(request, response);
        } finally {
            // после каждого запроса очищаем контекст, чтобы не запоминался польз. (чтобы отправлять всегда токен)
            SecurityContextHolder.clearContext();
        }
    }
}
