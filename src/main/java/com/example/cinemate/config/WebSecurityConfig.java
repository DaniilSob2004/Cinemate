package com.example.cinemate.config;

import com.example.cinemate.filter.JwtFilter;
import com.example.cinemate.handling.auth.OAuth2LoginAuthenticationSuccessHandler;
import com.example.cinemate.handling.error.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAsync
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${user_data.role}")
    private String userRole;

    @Value("${admin_data.role}")
    private String adminRole;

    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtFilter jwtFilter;
    private final JwtUnauthorizedEntryPoint jwtUnauthorizedEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler;
    private final UserDetailsService userDetailsService;  // для получения информации о польз. при аутентификации

    public WebSecurityConfig(
            BCryptPasswordEncoder passwordEncoder,
            JwtFilter jwtFilter,
            JwtUnauthorizedEntryPoint jwtUnauthorizedEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            OAuth2LoginAuthenticationSuccessHandler oAuth2LoginAuthenticationSuccessHandler,
            UserDetailsService userDetailsService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtFilter = jwtFilter;
        this.jwtUnauthorizedEntryPoint = jwtUnauthorizedEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.oAuth2LoginAuthenticationSuccessHandler = oAuth2LoginAuthenticationSuccessHandler;
        this.userDetailsService = userDetailsService;
    }

    // настраивает Spring Security для аутентификации через UserDetailsService и шифратор паролей
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // для всех пользователей
        for (String endpoint : Endpoint.getEndpointForAllUsers()) {
            http.authorizeRequests().mvcMatchers(endpoint).permitAll();
        }

        // для авторизованных пользователей
        for (String endpoint : Endpoint.getEndpointForAuthUsers()) {
            http.authorizeRequests().mvcMatchers(endpoint)
                    .access(String.format("hasAnyRole('%s')", userRole));
        }

        // для админов
        for (String endpoint : Endpoint.getEndpointForAdmin()) {
            http.authorizeRequests().mvcMatchers(endpoint)
                    .access(String.format("hasAnyRole('%s')", adminRole));
        }

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // без сессии (только JWT)
                .and()
                .csrf().disable()  // CSRF не нужен для REST API
                .formLogin().disable()  // отключить встроенную форму логина
                .exceptionHandling()
                    .authenticationEntryPoint(jwtUnauthorizedEntryPoint)  // обработка неаутентифицированных запросов (401)
                    .accessDeniedHandler(jwtAccessDeniedHandler)  // обработка запрещенных запросов (forbidden 403)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // добавляем наш фильтр
                .oauth2Login()
                    .successHandler(oAuth2LoginAuthenticationSuccessHandler);  // обработчик успешного входа через Google
    }
}
