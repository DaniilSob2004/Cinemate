package com.example.cinemate.service.auth.external;

import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class BaseOAuthService implements OAuthService {

    protected final AuthProviderService authProviderService;
    protected final RegisterOAuthService registerOAuthService;
    protected final OAuthUserMapper oAuthUserMapper;

    public BaseOAuthService(AuthProviderService authProviderService, RegisterOAuthService registerOAuthService, OAuthUserMapper oAuthUserMapper) {
        this.authProviderService = authProviderService;
        this.registerOAuthService = registerOAuthService;
        this.oAuthUserMapper = oAuthUserMapper;
    }

    @Override
    public String processAuth(OAuth2User oauthUser) { return ""; }
}
