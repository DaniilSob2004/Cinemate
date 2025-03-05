package com.example.cinemate.service.business.userservice;

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
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Service
public class CurrentUserService {

    private final AppUserService appUserService;
    private final SaveUserDataService saveUserDataService;
    private final JwtTokenService jwtTokenService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final UserDetailsCacheService userDetailsCacheService;
    private final AppUserMapper appUserMapper;

    public CurrentUserService(AppUserService appUserService, SaveUserDataService saveUserDataService, JwtTokenService jwtTokenService, AccessJwtTokenService accessJwtTokenService, UserDetailsCacheService userDetailsCacheService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.saveUserDataService = saveUserDataService;
        this.jwtTokenService = jwtTokenService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.userDetailsCacheService = userDetailsCacheService;
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

        // изменён ли email
        boolean isEdit = saveUserDataService.saveEmail(appUser.getEmail(), userUpdateDto.getEmail(), !appUserJwtDto.getProvider().isEmpty());
        if (isEdit) {
            userDetailsCacheService.remove(appUser.getId().toString());
        }

        // проверяем и изменяем username у user (если необходимо)
        saveUserDataService.saveUsername(appUser, userUpdateDto.getUsername());

        // обновляем данные
        saveUserDataService.saveUser(appUser, userUpdateDto);

        appUserService.save(appUser);
    }
}
