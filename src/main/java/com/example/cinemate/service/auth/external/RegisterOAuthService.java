package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.OAuthUserDto;
import com.example.cinemate.exception.auth.OAuthException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.ExternalAuth;
import com.example.cinemate.service.auth.RegisterService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.business_db.externalauthservice.ExternalAuthService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class RegisterOAuthService {

    private final AppUserService appUserService;
    private final ExternalAuthService externalAuthService;
    private final RegisterService registerService;
    private final AppUserMapper appUserMapper;

    public RegisterOAuthService(AppUserService appUserService, ExternalAuthService externalAuthService, RegisterService registerService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.externalAuthService = externalAuthService;
        this.registerService = registerService;
        this.appUserMapper = appUserMapper;
    }

    @Transactional
    public String registerUser(final OAuthUserDto oAuthUserDto) {
        oAuthUserDto.setEmail(oAuthUserDto.getEmail().toLowerCase());

        // если пользователь есть
        AppUser user = appUserService.findByEmail(oAuthUserDto.getEmail()).orElse(null);
        if (user != null) {
            // есть ли уже авторизация через другой внешний провайдер
            if (!externalAuthService.existsByProviderAndExternalId(
                    oAuthUserDto.getProvider().getName(),
                    oAuthUserDto.getExternalId()
            )) {
                throw new OAuthException("This email is already associated with another OAuth provider");
            }
        }
        else {  // если пользователь нет, то создаём
            user = this.createNewUserFromOAuth(oAuthUserDto);
        }

        return registerService.authenticateAndGenerateToken(user.getId());  // авторизация и генерация токена
    }

    private AppUser createNewUserFromOAuth(final OAuthUserDto oAuthUserDto) {
        AppUser user = appUserMapper.toAppUser(oAuthUserDto);
        registerService.createUser(user);
        this.createExternalAuth(oAuthUserDto, user);
        return user;
    }

    private void createExternalAuth(final OAuthUserDto oAuthUserDto, final AppUser user) {
        var newExternalAuth = new ExternalAuth(null, user, oAuthUserDto.getProvider(), oAuthUserDto.getExternalId(), LocalDateTime.now());
        externalAuthService.save(newExternalAuth);
    }
}
