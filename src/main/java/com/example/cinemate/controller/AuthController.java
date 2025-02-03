package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.UserEmailNotFoundException;
import com.example.cinemate.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.AUTH)
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = Endpoint.LOGIN)
    public ResponseEntity<?> login(HttpServletRequest request) throws UserEmailNotFoundException {

        // проверяем, есть ли токен в заголовке и валидный ли он
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token != null) {
            return ResponseEntity.badRequest().body("Already authenticated");
        }

        // Basic authentication (получаем логин и пароль)
        LoginRequestDto loginRequestDto = authService.getBaseAuthDataFromHeader(request).orElse(null);
        if (loginRequestDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Basic Authentication");
        }

        // аутентификация и генерация токена
        try {
            Logger.info("(Authentication user) Email: " + loginRequestDto.getEmail() + ", Password: " + loginRequestDto.getPassword());

            token = authService.loginUser(loginRequestDto);

            Logger.info("User authenticated!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (BadCredentialsException | UserEmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
