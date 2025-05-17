package com.example.cinemate.utils;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.dto.common.PagedResponse;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

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

    public ResponseEntity<?> handleContentSearch(final ContentSearchParamsDto dto, final Function<ContentSearchParamsDto, PagedResponse<?>> searchFunction) {
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info(dto);
            return ResponseEntity.ok(searchFunction.apply(dto));
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
