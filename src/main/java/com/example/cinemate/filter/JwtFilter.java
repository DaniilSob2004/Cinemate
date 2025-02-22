package com.example.cinemate.filter;

import com.example.cinemate.event.JwtFilterEvent;
import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// фильтрация HTTP-запросов и проверка JWT-токена
@Component
public class JwtFilter extends OncePerRequestFilter implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher eventPublisher;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            // вызываем событие (в publisher отправляется ошибка)
            var jwtFilterEvent = new JwtFilterEvent(this, request, response);
            eventPublisher.publishEvent(jwtFilterEvent);

            // если ответ не был отправлен
            if (!jwtFilterEvent.isResponseHandled()) {
                chain.doFilter(request, response);
            }
        } finally {
            // после каждого запроса очищаем контекст, чтобы не запоминался польз. (чтобы отправлять всегда токен)
            SecurityContextHolder.clearContext();
        }
    }

    @Override
    public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
