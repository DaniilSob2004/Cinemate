package com.example.cinemate.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
public class JwtFilterEvent extends ApplicationEvent {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    @Setter private boolean responseHandled = false;

    public JwtFilterEvent(Object source, HttpServletRequest request, HttpServletResponse response) {
        super(source);
        this.request = request;
        this.response = response;
    }
}
