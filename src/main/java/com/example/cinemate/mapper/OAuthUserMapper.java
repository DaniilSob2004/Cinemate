package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.OAuthUserDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.model.db.ExternalAuth;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Component
public class OAuthUserMapper {

    public OAuthUserDto toOAuthGoogleUserDto(final OAuth2User oauthUser, final AuthProvider provider, final String accessToken) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String firstname = oauthUser.getAttribute("given_name");
        String surname = oauthUser.getAttribute("family_name");
        String picture = oauthUser.getAttribute("picture");
        String externalId = oauthUser.getAttribute("sub");

        return new OAuthUserDto(externalId, email, username, firstname, surname, picture, provider, accessToken);
    }

    public OAuthUserDto toOAuthFacebookUserDto(final OAuth2User oauthUser, final AuthProvider provider, final String accessToken) {
        String email = oauthUser.getAttribute("email");
        String username = oauthUser.getAttribute("name");
        String externalId = oauthUser.getAttribute("id");

        String picture = "";
        try {
            var pictureObj = (Map<String, Object>) oauthUser.getAttribute("picture");
            if (pictureObj != null) {
                var data = (Map<String, Object>) pictureObj.get("data");
                if (data != null) {
                    picture = (String) data.get("url");
                }
            }
        } catch (Exception e) {
            picture = "";
        }

        return new OAuthUserDto(externalId, email, username, username, username, picture, provider, accessToken);
    }

    public ExternalAuth toExternalAuth(final OAuthUserDto oAuthUserDto, final AppUser user) {
        return new ExternalAuth(
                null,
                user,
                oAuthUserDto.getProvider(),
                oAuthUserDto.getExternalId(),
                LocalDateTime.now(),
                oAuthUserDto.getAccessToken()
        );
    }
}
