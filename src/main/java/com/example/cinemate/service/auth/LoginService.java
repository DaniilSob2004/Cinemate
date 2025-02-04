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
        return authService.authenticateAndGenerateToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }

    public Optional<LoginRequestDto> getBaseAuthDataFromHeader(final HttpServletRequest request) {
        String credentials = baseAuthUtils.getCredentialsFromHeader(request).orElse(null);
        if (credentials == null) {
            return Optional.empty();
        }

        String[] loginPassword = baseAuthUtils.getLoginPassword(credentials).orElse(null);
        if (loginPassword == null) {
            return Optional.empty();
        }

        return Optional.of(
                new LoginRequestDto(loginPassword[0], loginPassword[1])
        );
    }
}
