package com.example.cinemate.service.auth;

import com.example.cinemate.mapper.GoogleAuthMapper;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.model.db.ExternalAuth;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.authproviderservice.AuthProviderService;
import com.example.cinemate.service.busines.externalauthservice.ExternalAuthService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;

@Service
public class GoogleAuthService {

    private final ExternalAuthService externalAuthService;
    private final AuthProviderService authProviderService;
    private final AppUserService appUserService;
    private final RegisterService registerService;
    private final LoginService loginService;
    private final GoogleAuthMapper googleAuthMapper;

    public GoogleAuthService(ExternalAuthService externalAuthService, AuthProviderService authProviderService, AppUserService appUserService, RegisterService registerService, LoginService loginService, GoogleAuthMapper googleAuthMapper) {
        this.externalAuthService = externalAuthService;
        this.authProviderService = authProviderService;
        this.appUserService = appUserService;
        this.registerService = registerService;
        this.loginService = loginService;
        this.googleAuthMapper = googleAuthMapper;
    }

    public String processGoogleAuth(final OAuth2User oauthUser) {
        // получаем данные
        AuthProvider provider = authProviderService.findByName("google")
                .orElseThrow(() -> new RuntimeException("Google provider not found"));

        var googleUserAuthDto = googleAuthMapper.toGoogleUserAuthDto(oauthUser, provider);

        // есть ли пользователь в БД
        AppUser user = appUserService.findByEmail(googleUserAuthDto.getEmail()).orElse(null);

        // возвращаем токен
        return (user == null)
                ? this.register(googleUserAuthDto)
                : this.login(googleUserAuthDto.getEmail());
    }

    private String register(final GoogleUserAuthDto googleUserAuthDto) {
        Logger.info("User google register");

        // добавляем пользователя и роль
        String token = registerService.registerUser(googleUserAuthDto);

        // создаём запись в ExternalAuth
        AppUser user = appUserService.findByEmail(googleUserAuthDto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User after google registration not found"));
        var newExternalAuth = new ExternalAuth(null, user, googleUserAuthDto.getProvider(), googleUserAuthDto.getExternalId(), LocalDateTime.now());
        externalAuthService.save(newExternalAuth);

        return token;
    }

    private String login(final String email) {
        Logger.info("User google login");

        var loginRequestDto = new LoginRequestDto(email, null);
        return loginService.loginUser(loginRequestDto);
    }
}
