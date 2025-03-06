package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.ResponseAuthDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.model.AuthenticationRequest;
import com.example.cinemate.utils.BaseAuthUtil;
import com.example.cinemate.validate.user.LoginValidate;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LoginService {

    private final AuthService authService;
    private final LoginValidate loginValidate;
    private final BaseAuthUtil baseAuthUtil;

    public LoginService(AuthService authService, LoginValidate loginValidate, BaseAuthUtil baseAuthUtil) {
        this.authService = authService;
        this.loginValidate = loginValidate;
        this.baseAuthUtil = baseAuthUtil;
    }

    public ResponseAuthDto loginUser(final LoginRequestDto loginRequestDto) {
        Logger.info("-------- User Login (" + loginRequestDto.getEmail() + ") --------");
        var authRequest = new AuthenticationRequest(
                loginRequestDto.getEmail().toLowerCase(),
                loginRequestDto.getPassword(),
                false,
                ""
        );
        return authService.authenticateAndGenerateToken(authRequest);
    }

    public ResponseAuthDto loginUser(final HttpServletRequest request) {
        // Basic authentication (получаем логин и пароль)
        LoginRequestDto loginRequestDto = this.getBaseAuthLoginDataFromHeader(request)
                .orElseThrow(() -> new BadRequestException("Invalid Basic Authentication"));

        /* валидация данных */
        loginValidate.validateLoginRequestDto(loginRequestDto);

        return this.loginUser(loginRequestDto);
    }

    private Optional<LoginRequestDto> getBaseAuthLoginDataFromHeader(final HttpServletRequest request) {
        return baseAuthUtil.getCredentialsFromHeader(request)
            .flatMap(baseAuthUtil::getLoginPassword)
            .map(credentials -> new LoginRequestDto(credentials[0], credentials[1]));
    }
}
