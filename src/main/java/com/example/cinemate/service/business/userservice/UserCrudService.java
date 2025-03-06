package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.user.UserAddDto;
import com.example.cinemate.dto.user.UserAdminDto;
import com.example.cinemate.dto.user.UserUpdateAdminDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import com.example.cinemate.service.business_db.userroleservice.UserRoleService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserCrudService {

    private final AppUserService appUserService;
    private final UserRoleService userRoleService;
    private final ExternalAuthService externalAuthService;
    private final UpdateAdminUserService updateAdminUserService;
    private final SaveUserService saveUserService;
    private final UserDataValidate userDataValidate;
    private final AppUserMapper appUserMapper;

    public UserCrudService(AppUserService appUserService, UserRoleService userRoleService, ExternalAuthService externalAuthService, UpdateAdminUserService updateAdminUserService, SaveUserService saveUserService, UserDataValidate userDataValidate, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.userRoleService = userRoleService;
        this.externalAuthService = externalAuthService;
        this.updateAdminUserService = updateAdminUserService;
        this.saveUserService = saveUserService;
        this.userDataValidate = userDataValidate;
        this.appUserMapper = appUserMapper;
    }

    public UserAdminDto getById(final Integer id) {
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        String provider = externalAuthService.findByUserId(appUser.getId())
                .map(auth -> auth.getProvider().getName())
                .orElse("");

        List<String> roles = userRoleService.getRoleNames(appUser.getId());

        return appUserMapper.toUserAdminDto(appUser, provider, roles);
    }

    @Transactional
    public void updateById(final Integer id, final UserUpdateAdminDto userUpdateAdminDto) {
        Logger.info("User update id{" + id + "} - " + userUpdateAdminDto);

        // найти пользователя в БД
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        // валидация email (иначе исключение)
        boolean isUserDetailsCacheDel =
                userDataValidate.validateEmailForUpdate(appUser.getEmail(), userUpdateAdminDto.getEmail(), appUser.getEncPassword().isEmpty());

        // проверяем и изменяем password у user (если необходимо)
        updateAdminUserService.updateUserPassword(appUser, userUpdateAdminDto.getPassword());

        boolean isAllCacheUserDel =
                updateAdminUserService.updateUserRole(appUser, userUpdateAdminDto.getRoles())  // изменены ли роли
                || (appUser.getIsActive() != userUpdateAdminDto.isActive() && !userUpdateAdminDto.isActive());  // если заблокировали

        // проверяем и изменяем username у user (если необходимо)
        userUpdateAdminDto.setUsername(
                userDataValidate.normalizeUsername(userUpdateAdminDto.getUsername(), appUser.getEmail())
        );

        // удаляем все access, refresh токены и UserDetails этого пользователя
        if (isAllCacheUserDel) {
            updateAdminUserService.deleteAllUserCache(appUser.getId().toString());
        }
        else if (isUserDetailsCacheDel) {  // удаляем UserDetails этого пользователя
            updateAdminUserService.deleteUserDetailsCache(appUser.getId().toString());
        }

        // обновляем данные
        updateAdminUserService.saveUserData(appUser, userUpdateAdminDto);
        appUser.setIsActive(userUpdateAdminDto.isActive());

        appUserService.save(appUser);
    }

    @Transactional
    public void add(final UserAddDto userAddDto) {
        // валидация
        userDataValidate.validateUserExistence(userAddDto.getEmail());

        // добавление пользователя
        AppUser newUser = appUserMapper.toAppUser(userAddDto);
        saveUserService.createUser(newUser);
        saveUserService.createUserRoles(newUser, userAddDto.getRoles());
    }
}
