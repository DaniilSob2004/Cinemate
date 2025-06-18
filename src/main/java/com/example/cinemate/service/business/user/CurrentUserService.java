package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.user.UserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.redis.UserDetailsCacheService;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class CurrentUserService {

    private final AppUserService appUserService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final UserDetailsCacheService userDetailsCacheService;
    private final UpdateUserService updateUserService;
    private final UserDataValidate userDataValidate;
    private final UserMapper userMapper;

    public CurrentUserService(AppUserService appUserService, AccessJwtTokenService accessJwtTokenService, UserDetailsCacheService userDetailsCacheService, UpdateUserService updateUserService, UserDataValidate userDataValidate, UserMapper userMapper) {
        this.appUserService = appUserService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.updateUserService = updateUserService;
        this.userDataValidate = userDataValidate;
        this.userMapper = userMapper;
    }

    public UserDto getUser(final HttpServletRequest request) {
        var appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        AppUser appUser = appUserService.findById(appUserJwtDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id '" + appUserJwtDto.getId() + "' was not found..."));

        return userMapper.toUserDto(appUser, appUserJwtDto.getProvider());
    }

    @Transactional
    public void updateUser(final UserUpdateDto userUpdateDto, final HttpServletRequest request) {
        Logger.info("User update: " + userUpdateDto);

        // получить данные пользователя из токена
        AppUserJwtDto appUserJwtDto = accessJwtTokenService.extractAllDataByRequest(request);

        // найти пользователя в БД
        AppUser appUser = appUserService.findById(appUserJwtDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id '" + appUserJwtDto.getId() + "' was not found..."));

        // валидация email (иначе исключение)
        if (userDataValidate.validateEmailForUpdate(appUser.getEmail(), userUpdateDto.getEmail(), !appUserJwtDto.getProvider().isEmpty())) {
            userDetailsCacheService.remove(appUser.getId().toString());
        }

        // проверяем и изменяем username у user (если необходимо)
        userUpdateDto.setUsername(
                userDataValidate.normalizeUsername(userUpdateDto.getUsername(), appUser.getEmail())
        );

        // обновляем данные
        updateUserService.saveUserData(appUser, userUpdateDto);

        appUserService.save(appUser);
    }
}
