package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.OAuthUserDto;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuthUserMapper {

    public OAuthUserDto toOAuthGoogleUserDto(final OAuth2User oauthUser, final AuthProvider provider) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String firstname = oauthUser.getAttribute("given_name");
        String surname = oauthUser.getAttribute("family_name");
        String externalId = oauthUser.getAttribute("sub");

        return new OAuthUserDto(externalId, email, username, firstname, surname, provider);
    }

    public OAuthUserDto toOAuthFacebookUserDto(final OAuth2User oauthUser, final AuthProvider provider) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String externalId = oauthUser.getAttribute("id");

        return new OAuthUserDto(externalId, email, username, username, username, provider);
    }
}
