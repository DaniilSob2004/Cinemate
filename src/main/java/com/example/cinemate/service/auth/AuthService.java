package com.example.cinemate.service.auth;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.model.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Lazy  // (цикл. зависимость с WebSecurityConfig)
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserConvertDto appUserConvertDto;

    public String loginUser(final LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
        userDetailsService.loadUserByUsername(loginRequestDto.getEmail());

        // получаем пользователя из БД
        AppUser user = appUserService.findByEmail(loginRequestDto.getEmail()).orElse(null);
        AppUserJwtDto appUserJwtDto = appUserConvertDto.convertToAppUserJwtDto(user);

        return jwtTokenUtil.generateToken(appUserJwtDto);
    }

    public void authorizationUserByToken(final String token) {
        // извлекаем данные пользователя из токена
        AppUserJwtDto appUserJwtDto = jwtTokenUtil.extractAllUserData(token);

        Logger.info("(authorizationUserByToken) AppUserJwtDto: " + appUserJwtDto);

        // загружается информация о польз, чтобы установить его права доступа
        UserDetails userDetails = userDetailsService.loadUserByUsername(appUserJwtDto.getEmail());

        // создаем объект аутентификации
        var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // устанавливаем аутентификацию в контекст безопасности Spring
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Optional<String> tokenValidateFromHeader(final HttpServletRequest request) {
        String token = jwtTokenUtil.getTokenByAuthHeader(request).orElse(null);
        if (token != null) {
            if (jwtTokenUtil.validateToken(token)) {
                return Optional.of(token);
            }
        }
        return Optional.empty();
    }
}
