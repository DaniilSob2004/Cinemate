package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class GoogleAuthMapper {

    public GoogleUserAuthDto toGoogleUserAuthDto(final OAuth2User oauthUser, final AuthProvider provider) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String firstname = oauthUser.getAttribute("given_name");
        String surname = oauthUser.getAttribute("family_name");
        String externalId = oauthUser.getAttribute("sub");

        return new GoogleUserAuthDto(externalId, email, username, firstname, surname, provider);
    }
}
