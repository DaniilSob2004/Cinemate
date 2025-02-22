package com.example.cinemate.handling.error;

import com.example.cinemate.utils.SendResponseUtil;
import com.example.cinemate.dto.error.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// обработка неавторизованных запросов
@Component
public class JwtUnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private final SendResponseUtil sendResponseUtil;

    public JwtUnauthorizedEntryPoint(SendResponseUtil sendResponseUtil) {
        this.sendResponseUtil = sendResponseUtil;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Logger.error("Unauthorized error: " + authException.getMessage());

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "Unauthorized access",
                HttpStatus.UNAUTHORIZED.value()
        );

        sendResponseUtil.sendError(response, errorResponse);
    }
}
