package com.example.cinemate.filter;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.utils.SendResponseUtil;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// фильтрация предотвращения аутентификации
@Component
public class AuthPreventionFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final SendResponseUtil sendResponseUtil;

    public AuthPreventionFilter(JwtTokenService jwtTokenService, SendResponseUtil sendResponseUtil) {
        this.jwtTokenService = jwtTokenService;
        this.sendResponseUtil = sendResponseUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // если запрос на один из указанных 'endpoints'
        if (Endpoint.getEndpointForAllUsers().contains(path)) {
            // есть токен в заголовке и он валидный, то отправкляем ошибку
            String token = jwtTokenService.getValidateTokenFromHeader(request).orElse(null);
            if (token != null) {
                var errorResponseDto = new ErrorResponseDto("User already authenticated", HttpStatus.BAD_REQUEST.value());
                sendResponseUtil.sendError(response, errorResponseDto);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
