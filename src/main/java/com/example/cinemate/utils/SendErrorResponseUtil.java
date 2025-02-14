package com.example.cinemate.utils;

import com.example.cinemate.dto.error.ErrorResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SendErrorResponseUtil {
    public void sendError(final HttpServletResponse response, final ErrorResponseDto errorResponse) throws IOException {
        var objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        // устанавливаем заголовки, статус и отправляем
        response.setStatus(errorResponse.getStatus());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
