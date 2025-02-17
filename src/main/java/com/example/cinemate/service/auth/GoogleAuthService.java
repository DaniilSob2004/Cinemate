package com.example.cinemate.service.auth;

import com.example.cinemate.convert.GoogleAuthConvertDto;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.model.db.ExternalAuth;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.busines.authproviderservice.AuthProviderService;
import com.example.cinemate.service.busines.externalauthservice.ExternalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;

@Service
public class GoogleAuthService {

    @Autowired
    private ExternalAuthService externalAuthService;

    @Autowired
    private AuthProviderService authProviderService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private GoogleAuthConvertDto googleAuthConvertDto;

    public String processGoogleAuth(final OAuth2User oauthUser) {
        // получаем данные
        AuthProvider provider = authProviderService.findByName("google")
                .orElseThrow(() -> new RuntimeException("Google provider not found"));

        var googleUserAuthDto = googleAuthConvertDto.convertToGoogleUserAuthDto(oauthUser, provider);

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
