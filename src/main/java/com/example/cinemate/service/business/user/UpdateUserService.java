package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.service.redis.UserDetailsCacheService;
import com.example.cinemate.service.redis.token.AccessTokenRedisStorage;
import com.example.cinemate.service.redis.token.RefreshTokenRedisStorage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UpdateUserService {

    private final UserRoleService userRoleService;
    private final UserDetailsCacheService userDetailsCacheService;
    private final AccessTokenRedisStorage accessTokenRedisStorage;
    private final RefreshTokenRedisStorage refreshTokenRedisStorage;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UpdateUserService(UserRoleService userRoleService, UserDetailsCacheService userDetailsCacheService, AccessTokenRedisStorage accessTokenRedisStorage, RefreshTokenRedisStorage refreshTokenRedisStorage, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRoleService = userRoleService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.accessTokenRedisStorage = accessTokenRedisStorage;
        this.refreshTokenRedisStorage = refreshTokenRedisStorage;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void updateUserPassword(final AppUser appUser, final String newPassword) {
        if (!newPassword.isEmpty()) {
            // запрещаем смену пароля если пользователь атворизовался через внешние провайдеры
            if (appUser.getEncPassword() == null) {
                throw new BadRequestException("External-authenticated users cannot change password");
            }
            String newEncPassword = bCryptPasswordEncoder.encode(newPassword);
            if (!newEncPassword.equals(appUser.getEncPassword())) {
                userDetailsCacheService.remove(appUser.getId().toString());  // удаляем из кэша UserDetails этого пользователя
                appUser.setEncPassword(newEncPassword);
            }
        }
    }

    public boolean updateUserRole(final AppUser appUser, final List<String> updateUserRoles) {
        boolean isModified = false;

        if (!updateUserRoles.isEmpty()) {
            var currentUserRoles = userRoleService.getRoleNames(appUser.getId());

            // проходимся по всем update ролям, если у пользователя нет такой роли, добавляем в БД
            for (String updateUserRole : updateUserRoles) {
                if (!currentUserRoles.contains(updateUserRole)) {
                    userRoleService.saveByRoleName(appUser, updateUserRole);
                    if (!isModified) {
                        isModified = true;
                    }
                }
            }

            // проходимся по всем ролям пользователя, если роли нет такой в update, то удаляем
            for (String userRole : currentUserRoles) {
                if (!updateUserRoles.contains(userRole)) {
                    userRoleService.deleteByRoleName(appUser, userRole);
                    if (!isModified) {
                        isModified = true;
                    }
                }
            }
        }

        return isModified;  // изменены ли роли
    }

    public void deleteUserDetailsCache(final String userId) {
        userDetailsCacheService.remove(userId);
    }

    public void deleteAllUserCache(final String userId) {
        accessTokenRedisStorage.removeByUserId(userId);
        refreshTokenRedisStorage.removeByUserId(userId);
        userDetailsCacheService.remove(userId);
    }

    public void saveUserData(final AppUser appUser, final UserUpdateDto userUpdateDto) {
        appUser.setUsername(userUpdateDto.getUsername());
        appUser.setFirstname(userUpdateDto.getFirstname());
        appUser.setSurname(userUpdateDto.getSurname());
        appUser.setEmail(userUpdateDto.getEmail());
        appUser.setPhoneNum(userUpdateDto.getPhoneNum());
        appUser.setAvatar(userUpdateDto.getAvatar());
        appUser.setUpdatedAt(LocalDateTime.now());
    }
}
