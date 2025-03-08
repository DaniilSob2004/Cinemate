package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class GoogleAuthService extends BaseOAuthService {

    @Autowired
    public GoogleAuthService(AuthProviderService authProviderService, RegisterOAuthService registerOAuthService, OAuthUserMapper oAuthUserMapper) {
        super(authProviderService, registerOAuthService, oAuthUserMapper);
    }

    @Override
    public ResponseAuthDto processAuth(OAuth2User oauthUser, String accessToken) {
        Logger.info("Google auth");

        // получаем данные
        AuthProvider provider = authProviderService.findByName("google")  // используется кеш
                .orElseThrow(() -> new RuntimeException("Google provider not found"));
        var googleUserAuthDto = oAuthUserMapper.toOAuthGoogleUserDto(oauthUser, provider, accessToken);
        return registerOAuthService.registerUser(googleUserAuthDto);
    }
}
