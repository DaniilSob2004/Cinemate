package com.example.cinemate.service.business.userservice;

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
import com.example.cinemate.utils.StringUtil;
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
    private final AppUserMapper appUserMapper;

    public CurrentUserService(AppUserService appUserService, JwtTokenService jwtTokenService, AccessJwtTokenService accessJwtTokenService, UserDetailsCacheService userDetailsCacheService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.jwtTokenService = jwtTokenService;
        this.accessJwtTokenService = accessJwtTokenService;
        this.userDetailsCacheService = userDetailsCacheService;
        this.appUserMapper = appUserMapper;
    }

    public UserDto getCurrentUser(final HttpServletRequest request) {
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

        // получить токен и id пользователя
        String token = jwtTokenService.getValidateTokenFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing token"));

        Integer userId = accessJwtTokenService.getIdFromToken(token);

        // найти пользователя в БД
        AppUser appUser = appUserService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + userId + "' was not found..."));

        // изменён ли email
        if (!appUser.getEmail().equals(userUpdateDto.getEmail())) {
            userDetailsCacheService.remove(userId.toString());  // удаляем из кэша UserDetails этого пользователя
        }
        // изменён ли username
        String username = userUpdateDto.getUsername();
        if (!appUser.getUsername().equals(username)) {
            if (!StringUtil.getFirstLetter(username).equals("@")) {
                userUpdateDto.setUsername(StringUtil.addSymbolInStart(username, "@"));
            }
        }

        // обновляем данные
        appUser.setUsername(userUpdateDto.getUsername());
        appUser.setFirstname(userUpdateDto.getFirstname());
        appUser.setSurname(userUpdateDto.getSurname());
        appUser.setEmail(userUpdateDto.getEmail());
        appUser.setPhoneNum(userUpdateDto.getPhoneNum());
        appUser.setAvatar(userUpdateDto.getAvatar());

        appUserService.save(appUser);
    }
}
