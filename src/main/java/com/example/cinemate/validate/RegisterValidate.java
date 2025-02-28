package com.example.cinemate.validate;

import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.exception.auth.*;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import org.springframework.stereotype.Component;

@Component
public class RegisterValidate {

    private final AppUserService appUserService;

    public RegisterValidate(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public void validateRegisterData(final RegisterRequestDto registerRequestDto) {
        this.validateEmail(registerRequestDto.getEmail());
        this.validatePasswords(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
        this.validateUserExistence(registerRequestDto.getEmail());
    }

    public void validateEmail(final String email) {
        if (!ValidateData.validateEmail(email)) {
            throw new InvalidEmailException("Email is not validated");
        }
    }

    private void validatePasswords(final String password, final String confirmPassword) {
        if (!ValidateData.validatePasswords(password, confirmPassword)) {
            throw new PasswordMismatchException("Passwords don’t match");
        }
    }

    private void validateUserExistence(final String email) {
        if (appUserService.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
    }
}
