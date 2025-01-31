package com.example.cinemate.config;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private UserDetailsService userDetailsService;  // для получения информации о польз. при аутентификации

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // шифратор паролей
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
        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // без сессии (только JWT)
                .and()
                .csrf().disable()  // отключить CSRF
                .formLogin().disable()  // отключить встроенную форму логина
                .authorizeRequests()
                    .antMatchers("/api/v1/auth/login", "/api/v1/auth/register").permitAll()  // доступ всем
                    .anyRequest().authenticated()  // доступ только авторизованым пользователям
                .and()
                .exceptionHandling().authenticationEntryPoint(new JwtAuthEntryPoint())  // обработка ошибок
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // добавляем наш фильтр
    }
}
