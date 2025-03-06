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
        userDataValidate.validateUserExistenceWithException(registerRequestDto.getEmail());
    }

    public void validateEmail(final String email) {
        if (!userDataValidate.validateEmail(email)) {
            throw new InvalidEmailException("Email is not validated");
        }
    }
}
