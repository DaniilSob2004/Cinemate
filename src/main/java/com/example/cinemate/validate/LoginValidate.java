package com.example.cinemate.validate;

import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.common.BadRequestException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class LoginValidate {

    private final Validator validator;

    public LoginValidate(Validator validator) {
        this.validator = validator;
    }

    public void validateLoginRequestDto(LoginRequestDto loginRequestDto) {
        // используем валидацию прописанную с помощью аннотаций в 'LoginRequestDto'
        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(loginRequestDto);  // объект валидации
        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("; "));

            throw new BadRequestException(errorMessages);
        }
    }
}
