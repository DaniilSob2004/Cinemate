package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.auth.jwt.AccessJwtTokenService;
import com.example.cinemate.service.auth.jwt.JwtTokenService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class CurrentUserService {

    private final AppUserService appUserService;
    private final JwtTokenService jwtTokenService;
    private final AccessJwtTokenService accessJwtTokenService;
    private final AppUserMapper appUserMapper;

    public CurrentUserService(AppUserService appUserService, JwtTokenService jwtTokenService, AccessJwtTokenService accessJwtTokenService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.jwtTokenService = jwtTokenService;
        this.accessJwtTokenService = accessJwtTokenService;
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
}
