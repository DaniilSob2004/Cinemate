package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.user.UserUpdateAdminDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.transaction.Transactional;

@Service
public class UserCrudService {

    private final AppUserService appUserService;
    private final ExternalAuthService externalAuthService;
    private final SaveUserDataService saveUserDataService;
    private final UpdateAdminUserService updateAdminUserService;
    private final AppUserMapper appUserMapper;

    public UserCrudService(AppUserService appUserService, ExternalAuthService externalAuthService, SaveUserDataService saveUserDataService, UpdateAdminUserService updateAdminUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.externalAuthService = externalAuthService;
        this.saveUserDataService = saveUserDataService;
        this.updateAdminUserService = updateAdminUserService;
        this.appUserMapper = appUserMapper;
    }

    public UserDto getUserById(final Integer id) {
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        String provider = externalAuthService.findByUserId(appUser.getId())
                .map(auth -> auth.getProvider().getName())
                .orElse("");

        return appUserMapper.toUserDto(appUser, provider);
    }

    @Transactional
    public void updateUserById(final Integer id, final UserUpdateAdminDto userUpdateAdminDto) {
        Logger.info("User update id{" + id + "} - " + userUpdateAdminDto);

        // найти пользователя в БД
        AppUser appUser = appUserService.findByIdWithoutIsActive(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        // изменён ли email
        boolean isUserDetailsCacheDel = saveUserDataService.saveEmail(appUser.getEmail(), userUpdateAdminDto.getEmail(), appUser.getEncPassword().isEmpty());

        // проверяем и изменяем password у user (если необходимо)
        updateAdminUserService.updateUserPassword(appUser, userUpdateAdminDto.getPassword());

        boolean isAllCacheUserDel =
                updateAdminUserService.updateUserRole(appUser, userUpdateAdminDto.getRoles())  // изменены ли роли
                || (appUser.getIsActive() != userUpdateAdminDto.isActive() && !userUpdateAdminDto.isActive());  // если заблокировали

        // проверяем и изменяем username у user (если необходимо)
        saveUserDataService.saveUsername(appUser, userUpdateAdminDto.getUsername());

        // удаляем все access, refresh токены и UserDetails этого пользователя
        if (isAllCacheUserDel) {
            updateAdminUserService.deleteAllUserCache(appUser.getId().toString());
        }
        else if (isUserDetailsCacheDel) {  // удаляем UserDetails этого пользователя
            updateAdminUserService.deleteUserDetailsCache(appUser.getId().toString());
        }

        // обновляем данные
        saveUserDataService.saveUser(appUser, userUpdateAdminDto);
        appUser.setIsActive(userUpdateAdminDto.isActive());

        appUserService.save(appUser);
    }
}
