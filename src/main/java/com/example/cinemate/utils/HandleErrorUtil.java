package com.example.cinemate.utils;

import com.example.cinemate.dto.error.ErrorResponseDto;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

@UtilityClass
public class HandleErrorUtil {

    // обработка ошибки для проверки неактивный ли пользователь
    public static ErrorResponseDto handleUserInactiveException(Exception e) {
        boolean isInactive = e.getMessage().contains("inactive");
        String message = isInactive ? "User is inactive..." : e.getMessage();
        int statusCode = isInactive ? HttpStatus.LOCKED.value() : HttpStatus.UNAUTHORIZED.value();
        return new ErrorResponseDto(message, statusCode);
    }
}
