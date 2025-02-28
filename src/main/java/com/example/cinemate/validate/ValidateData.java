package com.example.cinemate.validate;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateData {

    public boolean validateEmail(String email) {
        return !(email == null || email.trim().isEmpty());
    }

    public boolean validatePasswords(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
}
