package com.example.cinemate.service.auth.external;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface OAuthService {
    ResponseAuthDto processAuth(OAuth2User oauthUser, String accessToken);
}
