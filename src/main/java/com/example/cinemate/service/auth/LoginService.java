package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.utils.BaseAuthUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LoginService {

    private final AuthService authService;
    private final BaseAuthUtil baseAuthUtil;

    public LoginService(AuthService authService, BaseAuthUtil baseAuthUtil) {
        this.authService = authService;
        this.baseAuthUtil = baseAuthUtil;
    }

    public String loginUser(final LoginRequestDto loginRequestDto) {
        loginRequestDto.setEmail(loginRequestDto.getEmail().toLowerCase());  // в нижний регистр
        return authService.authenticateAndGenerateToken(loginRequestDto.getEmail(), loginRequestDto.getPassword(), false);
    }

    public Optional<LoginRequestDto> getBaseAuthDataFromHeader(final HttpServletRequest request) {
        return baseAuthUtil.getCredentialsFromHeader(request)
            .flatMap(baseAuthUtil::getLoginPassword)
            .map(loginPassword -> new LoginRequestDto(loginPassword[0], loginPassword[1]));
    }
}
