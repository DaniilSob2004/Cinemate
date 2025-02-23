package com.example.cinemate.service.auth;

import com.example.cinemate.mapper.GoogleAuthMapper;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.model.db.ExternalAuth;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import com.nimbusds.jose.util.Pair;
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

        // добавляем пользователя и роль (токен, user)
        Pair<String, AppUser> pair = registerService.registerUser(googleUserAuthDto);

        // создаём запись в ExternalAuth
        var newExternalAuth = new ExternalAuth(null, pair.getRight(), googleUserAuthDto.getProvider(), googleUserAuthDto.getExternalId(), LocalDateTime.now());
        externalAuthService.save(newExternalAuth);

        return pair.getLeft();
    }

    private String login(final String email) {
        Logger.info("User google login");

        var loginRequestDto = new LoginRequestDto(email, null);
        return loginService.loginUser(loginRequestDto);
    }
}
