package com.example.cinemate.service.auth.external;

import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.service.auth.RegisterService;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class FacebookAuthService implements OAuthService {

    private final AuthProviderService authProviderService;
    private final RegisterService registerService;
    private final OAuthUserMapper oAuthUserMapper;

    public FacebookAuthService(AuthProviderService authProviderService, RegisterService registerService, OAuthUserMapper oAuthUserMapper) {
        this.authProviderService = authProviderService;
        this.registerService = registerService;
        this.oAuthUserMapper = oAuthUserMapper;
    }

    @Override
    public String processAuth(OAuth2User oauthUser) {
        // получаем данные
        AuthProvider provider = authProviderService.findByName("facebook")
                .orElseThrow(() -> new RuntimeException("Facebook provider not found"));

        var facebookUserAuthDto = oAuthUserMapper.toOAuthFacebookUserDto(oauthUser, provider);
        return registerService.registerUserWithOAuth(facebookUserAuthDto);
    }
}
