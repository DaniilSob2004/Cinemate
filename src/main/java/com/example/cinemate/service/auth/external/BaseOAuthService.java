package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.mapper.OAuthUserMapper;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;

@RequiredArgsConstructor
public class BaseOAuthService implements OAuthService {

    protected final AuthProviderService authProviderService;
    protected final RegisterOAuthService registerOAuthService;
    protected final OAuthUserMapper oAuthUserMapper;

    @Override
    public ResponseAuthDto processAuth(OAuth2User oauthUser, String accessToken) { return null; }
}
