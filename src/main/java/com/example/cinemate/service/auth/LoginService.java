package com.example.cinemate.service.auth;

import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.utils.BaseAuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private AuthService authService;

    @Autowired
    private BaseAuthUtils baseAuthUtils;

    public String loginUser(final LoginRequestDto loginRequestDto) {
        loginRequestDto.setEmail(loginRequestDto.getEmail().toLowerCase());  // в нижний регистр
        return authService.authenticateAndGenerateToken(loginRequestDto.getEmail(), loginRequestDto.getPassword(), false);
    }

    public Optional<LoginRequestDto> getBaseAuthDataFromHeader(final HttpServletRequest request) {
        return baseAuthUtils.getCredentialsFromHeader(request)
            .flatMap(baseAuthUtils::getLoginPassword)
            .map(loginPassword -> new LoginRequestDto(loginPassword[0], loginPassword[1]));
    }
}
