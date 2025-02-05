package com.example.cinemate.handling.error;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// обработка forbidden запросов
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        Logger.error("Access Denied: " + accessDeniedException.getMessage());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "Forbidden: You do not have permission to access this resource",
                HttpStatus.FORBIDDEN.value()
        );

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
