package com.example.cinemate.service.auth.external;

import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class FacebookAuthService extends BaseOAuthService {

    public FacebookAuthService(AuthProviderService authProviderService, RegisterOAuthService registerOAuthService, OAuthUserMapper oAuthUserMapper) {
        super(authProviderService, registerOAuthService, oAuthUserMapper);
    }

    @Override
    public String processAuth(OAuth2User oauthUser, String accessToken) {
        Logger.info("Facebook auth");

        // получаем данные
        AuthProvider provider = authProviderService.findByName("facebook")  // используется кеш
                .orElseThrow(() -> new RuntimeException("Facebook provider not found"));
        var facebookUserAuthDto = oAuthUserMapper.toOAuthFacebookUserDto(oauthUser, provider, accessToken);
        return registerOAuthService.registerUser(facebookUserAuthDto);
    }
}
