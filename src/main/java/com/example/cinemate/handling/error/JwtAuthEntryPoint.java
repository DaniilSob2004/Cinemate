package com.example.cinemate.handling.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.cinemate.dto.error.ErrorResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
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
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Logger.error("Unauthorized error: " + authException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "Unauthorized access",
                HttpStatus.UNAUTHORIZED.value()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
