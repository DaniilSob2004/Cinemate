package com.example.cinemate.service.auth.external;

import org.springframework.security.oauth2.core.user.OAuth2User;

// Strategy
public interface OAuthService {
    String processAuth(OAuth2User oauthUser, String accessToken);
}
