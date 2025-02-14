package com.example.cinemate.handling.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Logger.info("-------- Google OAuth2 Login Authentication Success --------");

        if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    oauth2Token.getAuthorizedClientRegistrationId(),
                    oauth2Token.getName()
            );

            if (authorizedClient != null) {
                // Получаем токен
                String accessToken = authorizedClient.getAccessToken().getTokenValue();
                Logger.info("Access Token: " + accessToken);
            }
        }

        if (authentication != null) {
            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

            // AppUser
            String username = oauthUser.getAttribute("name");
            String firstname = oauthUser.getAttribute("given_name");
            String surname = oauthUser.getAttribute("family_name");
            String email = oauthUser.getAttribute("email");
            String password = "-";

            // ExternalAuth
            //AppUser user = new AppUser();
            String provider = "google";
            String providerId = oauthUser.getAttribute("sub");
            String accessToken = oauthUser.getAttribute("access_token");
            String refresh_token = oauthUser.getAttribute("refresh_token");
        }
    }
}
