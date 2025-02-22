package com.example.cinemate.handling.error;

import com.example.cinemate.dto.error.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

// Для глобальной обработки исключений
@ControllerAdvice
public class GlobalExceptionHandler {

    // метод будет обрабатывать исключения типа NoHandlerFoundException (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFound(NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto("Endpoint not found", HttpStatus.NOT_FOUND.value())
        );
    }
}
