package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CurrentUserService {

    private final AppUserService appUserService;
    private final AuthService authService;
    private final AppUserMapper appUserMapper;

    public CurrentUserService(AppUserService appUserService, AuthService authService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.authService = authService;
        this.appUserMapper = appUserMapper;
    }

    public UserDto getCurrentUser(final HttpServletRequest request) {
        String token = authService.tokenValidateFromHeader(request)
                .orElseThrow(() -> new UnauthorizedException("Invalid or missing token"));

        Integer id = authService.getUserIdByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));

        AppUser appUser = appUserService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        return appUserMapper.toUserDto(appUser);
    }
}
