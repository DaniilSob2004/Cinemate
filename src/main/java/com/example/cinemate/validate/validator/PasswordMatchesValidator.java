package com.example.cinemate.validate.validator;

import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.validate.annotation.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterRequestDto> {

    private String message;  // сообщение из аннотации

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(RegisterRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getPassword() == null || !dto.getPassword().equals(dto.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();  // отключение стандартного сообщения

            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("confirmPassword")  // сообщение будет связано с полем 'confirmPassword'
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
