package com.example.cinemate.config;

import com.example.cinemate.filter.JwtFilter;
import com.example.cinemate.handling.auth.OAuth2LoginAuthenticationSuccessHandler;
import com.example.cinemate.handling.error.JwtAccessDeniedHandler;
import com.example.cinemate.handling.error.JwtAuthEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${user_data.role}")
    private String userRole;

    @Value("${admin_data.role}")
    private String adminRole;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private UserDetailsService userDetailsService;  // для получения информации о польз. при аутентификации

    // шифратор паролей
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //
    @Bean
    public OAuth2LoginAuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2LoginAuthenticationSuccessHandler();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManager();
    }

    // настраивает Spring Security для аутентификации через UserDetailsService и шифратор паролей
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
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
                .csrf().disable()  // отключить CSRF
                .formLogin().disable()  // отключить встроенную форму логина
                .exceptionHandling()
                    .authenticationEntryPoint(jwtAuthEntryPoint)  // обработка неаутентифицированных запросов (401)
                    .accessDeniedHandler(jwtAccessDeniedHandler)  // обработка запрещенных запросов (forbidden 403)
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // добавляем наш фильтр

                .oauth2Login()
                    .successHandler(oAuth2AuthenticationSuccessHandler());  // обработчик успешного входа через Google

                /*.oauth2Login()
                    .successHandler(new OAuth2LoginAuthenticationSuccessHandler())
                    //.defaultSuccessUrl("/api/v1/auth/google", true)  // После успешной авторизации перенаправляем сюда
                    .failureUrl("/login?error")
                .and()
                .logout()
                    .logoutUrl("/api/v1/auth/logout") // URL для выхода
                    .clearAuthentication(true) // Очистка аутентификации
                    .invalidateHttpSession(true) // Очистка сессии
                    .deleteCookies("JSESSIONID", "oauth2_token"); // Удалить cookies
                 */
    }
}
