package com.example.cinemate.utils;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SendResponseUtil {

    private final ObjectMapper objectMapper;

    public SendResponseUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void sendError(final HttpServletResponse response, final ErrorResponseDto errorResponse) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // устанавливаем заголовки, статус и отправляем
        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    public void sendData(final HttpServletResponse response, final ResponseAuthDto responseAuthDto) throws IOException {
        String jsonResponse = objectMapper.writeValueAsString(responseAuthDto);

        // устанавливаем заголовки, статус и отправляем
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
