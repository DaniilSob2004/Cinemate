package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
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
    private final JwtTokenService jwtTokenService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final UserDetailsCacheService userDetailsCacheService;
    private final UpdateUserService updateUserService;
    private final UserDataValidate userDataValidate;
    private final AppUserMapper appUserMapper;

    public CurrentUserService(AppUserService appUserService, JwtTokenService jwtTokenService, AccessJwtTokenService accessJwtTokenService, UserDetailsCacheService userDetailsCacheService, UpdateUserService updateUserService, UserDataValidate userDataValidate, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.jwtTokenService = jwtTokenService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.updateUserService = updateUserService;
        this.userDataValidate = userDataValidate;
        this.appUserMapper = appUserMapper;
    }

    public UserDto getUser(final HttpServletRequest request) {
        String token = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing token"));

        var appUserJwtDto = accessJwtTokenService.extractAllData(token);

        AppUser appUser = appUserService.findById(appUserJwtDto.getId())
                .orElseThrow(() -> new UserNotFoundException("User with id '" + appUserJwtDto.getId() + "' was not found..."));

        return appUserMapper.toUserDto(appUser, appUserJwtDto.getProvider());
    }

    @Transactional
    public void updateUser(final UserUpdateDto userUpdateDto, final HttpServletRequest request) {
        Logger.info("User update: " + userUpdateDto);

        // получить токен и данные пользователя из токена
        String token = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing token"));

        AppUserJwtDto appUserJwtDto = accessJwtTokenService.extractAllData(token);

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
