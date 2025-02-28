package com.example.cinemate.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletResponse;

@Getter
public class StartOAuthEvent extends ApplicationEvent {

    private final OAuth2User oauthUser;
    private final String accessToken;
    private final String provider;
    private final HttpServletResponse response;
    @Setter private boolean responseHandled;

    public StartOAuthEvent(Object source, OAuth2User oauthUser, String accessToken, String provider, HttpServletResponse response) {
        super(source);
        this.oauthUser = oauthUser;
        this.accessToken = accessToken;
        this.provider = provider;
        this.response = response;
        this.responseHandled = false;
    }
}
