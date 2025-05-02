package com.example.cinemate.validate.user;

import com.example.cinemate.exception.auth.InvalidEmailException;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.exception.auth.UserInactiveException;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.model.db.AppUser;
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

    public void validateIsActiveUser(final AppUser user) {
        if (!user.getIsActive()) {
            throw new UserInactiveException("User is inactive");
        }
    }

    public void validateIsNotHaveProvider(final boolean isAuthProvider) {
        if (isAuthProvider) {
            throw new BadRequestException("External-authenticated users cannot change email or password");
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
            this.validateIsNotHaveProvider(isAuthProvider);

            // ошибка если такой email уже есть
            this.validateUserExistence(newEmail);

            return true;
        }
        return false;
    }

    public String normalizeUsername(String newUsername, final String email) {
        if (newUsername.isEmpty()) {
            newUsername = StringUtil.addSymbolInStart(
                    StringUtil.getUsernameFromEmail(email), "@"
            );
        }
        if (!StringUtil.getFirstLetter(newUsername).equals("@")) {
            newUsername = newUsername.replace(' ', '_');
            newUsername = StringUtil.addSymbolInStart(newUsername, "@");
        }
        return newUsername.toLowerCase();
    }
}
