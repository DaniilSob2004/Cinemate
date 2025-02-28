package com.example.cinemate.service.business.userservice;

import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import org.springframework.stereotype.Service;

@Service
public class UserCrudService {

    private final AppUserService appUserService;
    private final ExternalAuthService externalAuthService;
    private final AppUserMapper appUserMapper;

    public UserCrudService(AppUserService appUserService, ExternalAuthService externalAuthService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.externalAuthService = externalAuthService;
        this.appUserMapper = appUserMapper;
    }

    public UserDto getUserById(final Integer id) {
        AppUser appUser = appUserService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id '" + id + "' was not found..."));

        String provider = externalAuthService.findByUserId(appUser.getId())
                .map(auth -> auth.getProvider().getName())
                .orElse("");

        return appUserMapper.toUserDto(appUser, provider);
    }
}
