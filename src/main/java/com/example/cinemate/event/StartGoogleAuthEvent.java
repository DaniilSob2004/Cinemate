package com.example.cinemate.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.oauth2.core.user.OAuth2User;

import javax.servlet.http.HttpServletResponse;

@Getter
public class StartGoogleAuthEvent extends ApplicationEvent {

    private final OAuth2User oauthUser;
    private final HttpServletResponse response;
    @Setter private boolean responseHandled = false;

    public StartGoogleAuthEvent(Object source, OAuth2User oauthUser, HttpServletResponse response) {
        super(source);
        this.oauthUser = oauthUser;
        this.response = response;
    }
}
