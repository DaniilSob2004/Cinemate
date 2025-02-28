package com.example.cinemate.service.auth;

import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.AuthenticationRequest;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.service.auth.userdetail.UserDetailsServiceImpl;
import com.example.cinemate.utils.JwtTokenUtil;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AppUserMapper appUserMapper;

    public AuthService(
            @Lazy AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            UserDetailsServiceImpl userDetailsService,
            AppUserMapper appUserMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.appUserMapper = appUserMapper;
    }

    public void authorizationUserByToken(final String token) {
        // извлекаем данные пользователя из токена
        AppUserJwtDto appUserJwtDto = jwtTokenUtil.extractAllUserData(token);

        Logger.info("(authorizationUserByToken) AppUserJwtDto: " + appUserJwtDto);

        // загружается информация о польз, чтобы установить его права доступа
        UserDetails userDetails = userDetailsService.loadUserById(appUserJwtDto.getId());

        if (this.addUserDetailsToCache(userDetails)) {
            this.authenticateUserWithoutPassword(userDetails);  // аутентификация пользователя без пароля
            return;
        }

        // исключение, если пользователь не найден
        throw new UserNotFoundException("User '" + appUserJwtDto.getEmail() + "' was not found");
    }

    public String authenticateAndGenerateToken(@NonNull final AuthenticationRequest authRequest) {
        // аутентификация пользователя
        UserDetails userDetails = this.getAuthenticatedUserDetails(authRequest);

        // после успешной аутентификации добавляем в кэш и генерация токена
        if (this.addUserDetailsToCache(userDetails)) {
            AppUserJwtDto appUserJwtDto = appUserMapper.toAppUserJwtDto(userDetails, authRequest.getProvider());
            return jwtTokenUtil.generateToken(appUserJwtDto);
        }

        // исключение, если пользователь не найден
        throw new UserNotFoundException(
                authRequest.isId()
                ? "User '" + authRequest.getUsernameOrId() + "' was not found"
                : "User with id '" + authRequest.getUsernameOrId() + "' was not found");
    }

    public Optional<String> tokenValidateFromHeader(final HttpServletRequest request) {
        return jwtTokenUtil.getTokenByAuthHeader(request)
                .filter(jwtTokenUtil::validateToken);
    }

    public Optional<AppUserJwtDto> getUserDataByToken(final String token) {
        return Optional.ofNullable(token).map(jwtTokenUtil::extractAllUserData);
    }

    private boolean addUserDetailsToCache(final UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            userDetailsService.addUserToCache(customUserDetails.getId(), customUserDetails);
            return true;
        }
        return false;
    }

    private UserDetails getAuthenticatedUserDetails(final AuthenticationRequest authRequest) {
        UserDetails userDetails;
        if (authRequest.getPassword() == null) {  // если не нужно проверять пароль
            // загружает инфу (роли и данные) о польз.
            userDetails = authRequest.isId()
                    ? userDetailsService.loadUserById(Integer.valueOf(authRequest.getUsernameOrId()))  // по id
                    : userDetailsService.loadUserByUsername(authRequest.getUsernameOrId());  // по email
            this.authenticateUserWithoutPassword(userDetails);
        }
        else {  // с паролем
            userDetails = this.authenticateUser(authRequest.getUsernameOrId(), authRequest.getPassword());
        }
        return userDetails;
    }

    private UserDetails authenticateUser(final String username, final String password) {
        // аутентификацию пользователя (логин, пароль, роли)
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        // устанавливаем аутентификацию в контекст безопасности Spring
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (UserDetails) authentication.getPrincipal();
    }

    private void authenticateUserWithoutPassword(final UserDetails userDetails) {
        // создаем объект аутентификации
        var usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // устанавливаем аутентификацию в контекст безопасности Spring
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}
