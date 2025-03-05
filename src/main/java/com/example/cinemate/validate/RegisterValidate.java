package com.example.cinemate.validate;

import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.exception.auth.*;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidate {

    private final UserDataValidate userDataValidate;

    public RegisterValidate(UserDataValidate userDataValidate) {
        this.userDataValidate = userDataValidate;
    }

    public void validateRegisterData(final RegisterRequestDto registerRequestDto) {
        this.validatePasswords(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
        userDataValidate.validateUserExistenceWithException(registerRequestDto.getEmail());
    }

    public void validateEmail(final String email) {
        if (!userDataValidate.validateEmail(email)) {
            throw new InvalidEmailException("Email is not validated");
        }
    }

    private void validatePasswords(final String password, final String confirmPassword) {
        if (!userDataValidate.validatePasswords(password, confirmPassword)) {
            throw new PasswordMismatchException("Passwords don’t match");
        }
    }
}
