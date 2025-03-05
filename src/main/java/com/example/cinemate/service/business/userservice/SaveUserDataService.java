package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.utils.StringUtil;
import com.example.cinemate.validate.UserDataValidate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaveUserDataService {

    private final UserDataValidate userDataValidate;

    public SaveUserDataService(UserDataValidate userDataValidate) {
        this.userDataValidate = userDataValidate;
    }

    public void saveUsername(final AppUser appUser, String newUsername) {
        if (newUsername.isEmpty()) {
            appUser.setUsername(StringUtil.addSymbolInStart(
                    StringUtil.getUsernameFromEmail(appUser.getEmail()), "@").toLowerCase()
            );
        }
        else if (!appUser.getUsername().equals(newUsername)) {
            if (!StringUtil.getFirstLetter(newUsername).equals("@")) {
                newUsername = StringUtil.addSymbolInStart(newUsername, "@").toLowerCase();
            }
            appUser.setUsername(newUsername);
        }
    }

    public boolean saveEmail(final String email, final String newEmail, final boolean isAuthProvider) {
        if (!email.equals(newEmail)) {
            // запрещаем смену email если пользователь атворизовался через внешние провайдеры
            if (isAuthProvider) {
                throw new BadRequestException("External-authenticated users cannot change their email");
            }
            // запрещаем изменение если такой email уже есть
            userDataValidate.validateUserExistenceWithException(newEmail);

            return true;
        }
        return false;
    }

    public void saveUser(final AppUser appUser, final UserUpdateDto userUpdateDto) {
        appUser.setFirstname(userUpdateDto.getFirstname());
        appUser.setSurname(userUpdateDto.getSurname());
        appUser.setEmail(userUpdateDto.getEmail());
        appUser.setPhoneNum(userUpdateDto.getPhoneNum());
        appUser.setAvatar(userUpdateDto.getAvatar());
        appUser.setUpdatedAt(LocalDateTime.now());
    }
}
