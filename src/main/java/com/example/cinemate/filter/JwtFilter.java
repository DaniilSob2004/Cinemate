package com.example.cinemate.filter;

import com.example.cinemate.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${security.header_auth_name}")
    private String headerAuthName;

    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            Logger.info("JWT auth filter header: " + request.getHeader(headerAuthName));

            // проверяем, есть ли токен в заголовке, валидный ли он,и авторизовываем пользователя
            authService.tokenValidateFromHeader(request).ifPresent(token -> authService.authorizationUserByToken(token));

            chain.doFilter(request, response);
        } finally {
            // после каждого запроса очищаем контекст, чтобы не запоминался польз. (чтобы вводить всегда токен)
            SecurityContextHolder.clearContext();
        }
    }
}
