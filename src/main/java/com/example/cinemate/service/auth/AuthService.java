package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.dto.auth.RefreshTokenDto;
import com.example.cinemate.mapper.user.UserMapper;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.AuthenticationRequest;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.service.auth.jwt.*;
import com.example.cinemate.service.auth.userdetail.UserDetailsServiceImpl;
import com.example.cinemate.service.redis.UserProviderStorage;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AccessJwtTokenService accessJwtTokenService;
    private final RefreshJwtTokenService refreshJwtTokenService;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserProviderStorage userProviderStorage;
    private final UserMapper userMapper;

    public AuthService(
            @Lazy AuthenticationManager authenticationManager,
            AccessJwtTokenService accessJwtTokenService,
            RefreshJwtTokenService refreshJwtTokenService,
            UserDetailsServiceImpl userDetailsService, UserProviderStorage userProviderStorage,
            UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.accessJwtTokenService = accessJwtTokenService;
        this.refreshJwtTokenService = refreshJwtTokenService;
        this.userDetailsService = userDetailsService;
        this.userProviderStorage = userProviderStorage;
        this.userMapper = userMapper;
    }

    public void authorizationUserByToken(final String token) {
        // извлекаем данные пользователя из токена
        AppUserJwtDto appUserJwtDto = accessJwtTokenService.extractAllData(token);

        Logger.info("(authorizationUserByToken) AppUserJwtDto: " + appUserJwtDto);

        // загружается информация о польз, чтобы установить его права доступа
        UserDetails userDetails = userDetailsService.loadUserById(appUserJwtDto.getId(), appUserJwtDto.getRoles());

        if (this.addUserDetailsToCache(userDetails, appUserJwtDto.getProvider())) {
            this.authenticateUserWithoutPassword(userDetails);  // аутентификация пользователя без пароля
            return;
        }

        // исключение, если пользователь не найден
        throw new UserNotFoundException("User '" + userDetails.getUsername() + "' was not found");
    }

    public ResponseAuthDto authenticateAndGenerateToken(@NonNull final AuthenticationRequest authRequest) {
        // аутентификация пользователя
        UserDetails userDetails = this.getAuthenticatedUserDetails(authRequest);

        // после успешной аутентификации добавляем в кэш и генерируем два токена
        if (this.addUserDetailsToCache(userDetails, authRequest.getProvider())) {
            var appUserJwtDto = userMapper.toAppUserJwtDto(userDetails, authRequest.getProvider());
            var refreshTokenDto = new RefreshTokenDto(appUserJwtDto.getId());
            return new ResponseAuthDto(
                    accessJwtTokenService.generateAndSaveToken(appUserJwtDto),
                    refreshJwtTokenService.generateAndSaveToken(refreshTokenDto)
            );
        }

        // исключение, если пользователь не найден
        throw new UserNotFoundException(
                authRequest.isId()
                ? "User '" + authRequest.getUsernameOrId() + "' was not found"
                : "User with id '" + authRequest.getUsernameOrId() + "' was not found");
    }

    private boolean addUserDetailsToCache(final UserDetails userDetails, final String provider) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            userDetailsService.addUserToCache(customUserDetails.getId(), customUserDetails);
            userProviderStorage.addToStorage(customUserDetails.getId().toString(), provider);
            return true;
        }
        return false;
    }

    private UserDetails getAuthenticatedUserDetails(final AuthenticationRequest authRequest) {
        UserDetails userDetails;
        if (authRequest.getPassword() == null) {  // если не нужно проверять пароль
            // загружает инфу (роли и данные) о польз.
            userDetails = authRequest.isId()
                    ? userDetailsService.loadUserById(Integer.valueOf(authRequest.getUsernameOrId()), null)  // по id
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
