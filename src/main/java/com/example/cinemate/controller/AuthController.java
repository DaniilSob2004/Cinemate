package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.auth.AuthResponseDto;
import com.example.cinemate.dto.auth.LoginRequestDto;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.auth.*;
import com.example.cinemate.service.auth.AuthService;
import com.example.cinemate.service.auth.LoginService;
import com.example.cinemate.service.auth.RegisterService;

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

    @Autowired
    private LoginService loginService;

    @Autowired
    private RegisterService registerService;

    @PostMapping(value = Endpoint.LOGIN)
    public ResponseEntity<?> login(HttpServletRequest request) {
        // проверяем, есть ли токен в заголовке и валидный ли он
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token != null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponseDto("User already authenticated", HttpStatus.BAD_REQUEST.value()));
        }

        // Basic authentication (получаем логин и пароль)
        LoginRequestDto loginRequestDto = loginService.getBaseAuthDataFromHeader(request).orElse(null);
        if (loginRequestDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDto("Invalid Basic Authentication", HttpStatus.BAD_REQUEST.value()));
        }

        // аутентификация и генерация токена
        try {
            Logger.info("(Authentication user) Email: " + loginRequestDto.getEmail());

            token = loginService.loginUser(loginRequestDto);

            Logger.info("User authenticated!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (BadCredentialsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping(value = Endpoint.REGISTER)
    public ResponseEntity<?> register(HttpServletRequest request, @RequestBody RegisterRequestDto registerRequestDto) {
        // проверяем, есть ли токен в заголовке и валидный ли он
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token != null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponseDto("User already authenticated", HttpStatus.BAD_REQUEST.value()));
        }

        // регистрация и генерация токена
        try {
            Logger.info("Register request: " + registerRequestDto);

            token = registerService.registerUser(registerRequestDto);

            Logger.info("User registered!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value()));
        } catch (PasswordMismatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        } catch (BadCredentialsException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping(value = Endpoint.LOGOUT)
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
            Logger.info("Token in logout controller - " + token);
        }

        // TODO: Redis добавление токена в blacklist (чтобы до истечение срока нельзя было его использ.)

        return ResponseEntity.ok("Logout successful");
    }
}
