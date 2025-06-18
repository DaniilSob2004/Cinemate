package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.user.file.UserFilesBufferDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaveUserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final AmazonS3Service amazonS3Service;
    private final AppUserService appUserService;
    private final UserRoleService userRoleService;
    private final UserDataValidate userDataValidate;

    public SaveUserService(BCryptPasswordEncoder passwordEncoder, AmazonS3Service amazonS3Service, AppUserService appUserService, UserRoleService userRoleService, UserDataValidate userDataValidate) {
        this.passwordEncoder = passwordEncoder;
        this.amazonS3Service = amazonS3Service;
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.userDataValidate = userDataValidate;
    }

    public void uploadFilesAndUpdate(final AppUser user, final UserFilesBufferDto userFilesBufferDto, final String rootPathPrefix) {
        if (userFilesBufferDto.getAvatar() == null) {
            return;
        }

        // если аватарка была, то удаляем
        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            amazonS3Service.deleteFromS3(user.getAvatar());
        }

        // загружаем картинку в s3
        String avatarUrl = amazonS3Service.uploadAndGenerateKey(userFilesBufferDto.getAvatar(), rootPathPrefix);

        // сохранение пользователя
        user.setAvatar(avatarUrl);
        user.setUpdatedAt(LocalDateTime.now());

        appUserService.update(user);

        Logger.info("S3 files have been successfully uploaded and user has been updated: " + user.getId());
    }

    public AppUser createUser(final AppUser user) {
        this.prepareUserDataForSave(user);
        return appUserService.save(user);
    }

    public void createUserRoles(final AppUser user, List<String> rolesName) {
        for (String roleName : rolesName) {
            userRoleService.saveByRoleName(user, roleName);
        }
    }

    private void prepareUserDataForSave(final AppUser user) {
        user.setUsername(
                userDataValidate.normalizeUsername(user.getUsername(), user.getEmail())
        );
        user.setEmail(
                user.getEmail().toLowerCase()
        );
        if (user.getEncPassword() != null) {
            user.setEncPassword(
                    passwordEncoder.encode(user.getEncPassword())
            );
        }
    }
}
