package com.example.cinemate.validate.user;

import com.example.cinemate.exception.auth.InvalidEmailException;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.utils.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidate {

    private final AppUserService appUserService;

    public UserDataValidate(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public void validateEmail(final String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidEmailException("Email is not validated");
        }
    }

    public void validateUserExistence(final String email) {
        if (appUserService.existsByEmail(email)) {
            throw new UserAlreadyExistsException("This email already exists: " + email);
        }
    }

    public boolean validateEmailForUpdate(final String email, final String newEmail, final boolean isAuthProvider) {
        if (!email.equals(newEmail)) {
            // запрещаем сохранение email если пользователь атворизовался через внешние провайдеры
            if (isAuthProvider) {
                throw new BadRequestException("External-authenticated users cannot change their email");
            }
            // ошибка если такой email уже есть
            this.validateUserExistence(newEmail);

            return true;
        }
        return false;
    }

    public String normalizeUsername(String newUsername, final String username, final String email) {
        if (newUsername.isEmpty()) {
            newUsername = StringUtil.addSymbolInStart(
                    StringUtil.getUsernameFromEmail(email), "@"
            ).toLowerCase();
        }
        else if (!username.equals(newUsername)) {
            if (!StringUtil.getFirstLetter(newUsername).equals("@")) {
                newUsername = StringUtil.addSymbolInStart(newUsername, "@").toLowerCase();
            }
        }
        return newUsername;
    }
}
