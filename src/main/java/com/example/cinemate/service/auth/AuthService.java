package com.example.cinemate.service.auth;

import com.example.cinemate.convert.AppUserConvertDto;
import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.CustomUserDetails;
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
    private AppUserConvertDto appUserConvertDto;

    public void authorizationUserByToken(final String token) {
        // извлекаем данные пользователя из токена
        AppUserJwtDto appUserJwtDto = jwtTokenUtil.extractAllUserData(token);

        Logger.info("(authorizationUserByToken) AppUserJwtDto: " + appUserJwtDto);

        // загружается информация о польз, чтобы установить его права доступа
        UserDetails userDetails = userDetailsService.loadUserById(appUserJwtDto.getId());

        // если в кэше нет, то добавляем
        if (!userDetailsService.checkUserInCache(appUserJwtDto.getId())) {
            userDetailsService.addUserToCache(appUserJwtDto.getId(), userDetails);
        }

        // аутентификация пользователя без пароля
        this.authenticateUserWithoutPassword(userDetails);
    }

    public String authenticateAndGenerateToken(final String usernameOrId, final String password, final boolean isId) {
        // загружает инфу (роли и данные) о польз.
        UserDetails userDetails = isId
                ? userDetailsService.loadUserById(Integer.valueOf(usernameOrId))  // по id
                : userDetailsService.loadUserByUsername(usernameOrId);  // по username

        // аутентификация пользователя
        if (password == null) {  // если не нужно проверять пароль
            this.authenticateUserWithoutPassword(userDetails);
        }
        else {  // с паролем
            this.authenticateUser(userDetails, password);
        }

        // после успешной аутентификации добавляем в кэш
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            userDetailsService.addUserToCache(customUserDetails.getId(), userDetails);

            // генерация JWT-токена
            AppUserJwtDto appUserJwtDto = appUserConvertDto.convertToAppUserJwtDto(customUserDetails);
            return jwtTokenUtil.generateToken(appUserJwtDto);
        }

        // исключение, если пользователь не найден
        throw new UserNotFoundException(
                isId
                ? "User '" + usernameOrId + "' was not found"
                : "User with id '" + usernameOrId + "' was not found");
    }

    public Optional<String> tokenValidateFromHeader(final HttpServletRequest request) {
        return jwtTokenUtil.getTokenByAuthHeader(request)
                .filter(jwtTokenUtil::validateToken);
    }

    public Optional<Integer> getUserIdByToken(final String token) {
        return Optional.ofNullable(token).map(jwtTokenUtil::extractSubject);
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
