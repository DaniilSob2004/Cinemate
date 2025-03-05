package com.example.cinemate.validate;

import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidate {

    private final AppUserService appUserService;

    public UserDataValidate(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public boolean validateEmail(final String email) {
        return !(email == null || email.trim().isEmpty());
    }

    public boolean validatePasswords(final String password, final String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean validateUserExistence(final String email) {
        return appUserService.existsByEmail(email);
    }

    public void validateUserExistenceWithException(final String email) {
        if (this.validateUserExistence(email)) {
            throw new UserAlreadyExistsException("This email already exists: " + email);
        }
    }
}
