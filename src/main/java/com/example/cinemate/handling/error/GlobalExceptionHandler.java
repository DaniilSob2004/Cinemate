package com.example.cinemate.handling.error;

import com.example.cinemate.dto.error.*;
import com.example.cinemate.exception.auth.*;
import com.example.cinemate.exception.common.*;
import com.example.cinemate.exception.genre.GenresTestDataNotFoundException;
import com.example.cinemate.utils.HandleErrorUtil;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.validation.BindException;
import org.tinylog.Logger;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

// Глобальная обработка исключений
@RestControllerAdvice
public class GlobalExceptionHandler {

    // обработка исключения типа Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, HttpServletRequest request) {
        Logger.error("Unhandled exception at {} {}: {}", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }

    // обработка исключения типа NoHandlerFoundException (404)
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoHandlerFound() {
        Logger.warn("Endpoint not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto("Endpoint not found", HttpStatus.NOT_FOUND.value())
        );
    }

    // обработка исключения типа BadRequestException (400)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequest(BadRequestException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value())
        );
    }

    // обработка исключения типа EntityNotFoundException (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFound(EntityNotFoundException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        );
    }

    // обработка исключения типа BadCredentialsException и InternalAuthenticationServiceException (423, 401)
    @ExceptionHandler({BadCredentialsException.class, InternalAuthenticationServiceException.class})
    public ResponseEntity<ErrorResponseDto> handleBadCredentials(AuthenticationException ex) {
        Logger.warn(ex.getMessage());
        var errorResponseDto = HandleErrorUtil.handleUserInactiveException(ex);
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    // обработка исключения типа UserInactiveException (423)
    @ExceptionHandler(UserInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserInactive(UserInactiveException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.LOCKED).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.LOCKED.value())
        );
    }

    // обработка исключения типа UnauthorizedException (401)
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleUnauthorized(UnauthorizedException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.UNAUTHORIZED.value())
        );
    }

    // обработка исключения типа ContentAlreadyExistsException (409)
    @ExceptionHandler(ContentAlreadyExists.class)
    public ResponseEntity<ErrorResponseDto> handleContentAlreadyExists(ContentAlreadyExists ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.CONFLICT.value())
        );
    }

    // обработка исключения типа UserAlreadyExistsException (409)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.CONFLICT.value())
        );
    }

    // обработка исключения типа EmailSendException (409)
    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailSend(EmailSendException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value())
        );
    }

    // обработка исключения типа UserNotFoundException (404)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        );
    }

    // обработка исключения типа ContentNotFoundException (404)
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleContentNotFound(ContentNotFoundException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        );
    }

    // обработка исключения типа GenresTestDataNotFoundException (404)
    @ExceptionHandler(GenresTestDataNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleGenresTestDataNotFound(GenresTestDataNotFoundException ex) {
        Logger.warn(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value())
        );
    }


    // обработка исключения по валидации данных типа MethodArgumentNotValidException / (@RequestBody)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Logger.warn(ex.getMessage());
        return this.sendBadRequestErrorWithFields(ex.getBindingResult().getFieldErrors());
    }

    // обработка исключения по валидации query-параметров типа BindException / (@ModelAttribute)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponseDto> handleBindException(BindException ex) {
        Logger.warn(ex.getMessage());
        return this.sendBadRequestErrorWithFields(ex.getBindingResult().getFieldErrors());
    }

    // обработка исключения по валидации данных типа ConstraintViolationException
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));

        Logger.warn(errorMessage);

        var errorResponseDto = new ErrorResponseDto(
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }


    private ResponseEntity<ErrorResponseDto> sendBadRequestErrorWithFields(final List<FieldError> fieldErrors) {
        String errorMessage = fieldErrors
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));

        var errorResponseDto = new ErrorResponseDto(
                errorMessage,
                HttpStatus.BAD_REQUEST.value()
        );

        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
