package com.example.cinemate.service.auth;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import com.example.cinemate.service.auth.userdetail.UserDetailsServiceImpl;
import com.example.cinemate.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Lazy  // (цикл. зависимость с WebSecurityConfig)
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private AppUserConvertDto appUserConvertDto;

    public void authorizationUserByToken(final String token) {
        // извлекаем данные пользователя из токена
        AppUserJwtDto appUserJwtDto = jwtTokenUtil.extractAllUserData(token);

        Logger.info("(authorizationUserByToken) AppUserJwtDto: " + appUserJwtDto);

        // загружается информация о польз, чтобы установить его права доступа
        UserDetails userDetails = userDetailsService.loadUserByUsername(appUserJwtDto.getEmail());

        // аутентификация пользователя (логин, пароль, роли)
        this.authenticateUserWithoutPassword(userDetails);
    }

    public String authenticateAndGenerateToken(final String username, final String password) {
        // загружает инфу (роли и данные) о польз.
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // аутентификация пользователя (логин, пароль, роли)
        if (Objects.equals(password, "")) {  // внешние провайдеры (без пароля)
            this.authenticateUserWithoutPassword(userDetails);
        }
        else {  // с паролем
            this.authenticateUser(userDetails, password);
        }

        // после успешной аутентификации добавляем в кэш
        userDetailsService.addUserToCache(username, userDetails);

        // получаем пользователя с ролями
        AppUser user = appUserService.findByEmailWithRoles(username)
                .orElseThrow(() -> new UserNotFoundException("User '" + username + " not found after authentication"));

        // генерация JWT-токена
        AppUserJwtDto appUserJwtDto = appUserConvertDto.convertToAppUserJwtDto(user);
        return jwtTokenUtil.generateToken(appUserJwtDto);
    }

    public Optional<String> tokenValidateFromHeader(final HttpServletRequest request) {
        return jwtTokenUtil.getTokenByAuthHeader(request)
                .filter(jwtTokenUtil::validateToken);
    }

    public Optional<String> getTokenFromHeaderStr(final String tokenHeader) {
        return jwtTokenUtil.getTokenFromAuthHeaderStr(tokenHeader);
    }

    private void authenticateUser(final UserDetails userDetails, final String password) {
        // аутентификацию пользователя (логин, пароль, роли)
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities()
        );

        // устанавливаем аутентификацию в контекст безопасности Spring
        SecurityContextHolder.getContext().setAuthentication(
                authenticationManager.authenticate(usernamePasswordAuthenticationToken)
        );
    }

    private void authenticateUserWithoutPassword(final UserDetails userDetails) {
        // создаем объект аутентификации
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // устанавливаем аутентификацию в контекст безопасности Spring
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
