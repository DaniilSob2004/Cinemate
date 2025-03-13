package com.example.cinemate.handling.error;

import com.example.cinemate.dto.error.ErrorResponseDto;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.stream.Collectors;

// Для глобальной обработки исключений
@RestControllerAdvice
public class GlobalExceptionHandler {

    // метод будет обрабатывать исключения типа NoHandlerFoundException (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto("Endpoint not found", HttpStatus.NOT_FOUND.value())
        );
    }

    // метод будет обрабатывать исключения по валидации данных типа MethodArgumentNotValidException (для валидации @RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        return this.sendBadRequestError(ex.getBindingResult().getFieldErrors());
    }

    // для валидации query-параметров / @ModelAttribute
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex) {
        return this.sendBadRequestError(ex.getBindingResult().getFieldErrors());
    }

    private ResponseEntity<ErrorResponseDto> sendBadRequestError(List<FieldError> fieldErrors) {
        String errorMessage = fieldErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        var errorResponseDto = new ErrorResponseDto(
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponseDto);
    }
}
