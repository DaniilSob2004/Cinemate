package com.example.cinemate.controller;

import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.exception.UserEmailNotFoundException;
import com.example.cinemate.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequestDto loginRequestDto) throws UserEmailNotFoundException {

        Logger.info("Email: " + loginRequestDto.getEmail() + ", Password: " + loginRequestDto.getPassword());

        // проверяем, есть ли токен в заголовке и валидный ли он
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token != null) {
            return ResponseEntity.badRequest().body("Already authenticated");
        }

        // аутентификация и генерация токена
        try {
            Logger.info("Authentication user...");

            token = authService.loginUser(loginRequestDto);

            Logger.info("User authenticated!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (BadCredentialsException | UserEmailNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
