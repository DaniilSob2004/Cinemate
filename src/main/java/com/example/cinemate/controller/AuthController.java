package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.auth.*;
import com.example.cinemate.service.auth.LoginService;
import com.example.cinemate.service.auth.LogoutService;
import com.example.cinemate.service.auth.RegisterService;
import com.example.cinemate.service.auth.UpdateTokenService;
import com.example.cinemate.service.business.user.UpdatePasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.AUTH)
public class AuthController {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final UpdateTokenService updateTokenService;
    private final UpdatePasswordService updatePasswordService;
    private final LogoutService logoutService;

    public AuthController(LoginService loginService, RegisterService registerService, UpdateTokenService updateTokenService, UpdatePasswordService updatePasswordService, LogoutService logoutService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.updateTokenService = updateTokenService;
        this.updatePasswordService = updatePasswordService;
        this.logoutService = logoutService;
    }

    @PostMapping(value = Endpoint.LOGIN)
    public ResponseEntity<?> login(HttpServletRequest request) {
        ResponseAuthDto responseAuthDto = loginService.loginUser(request);  // аутентификация и генерация токенов
        Logger.info("User authenticated!");
        return ResponseEntity.ok(responseAuthDto);  // отправляем два токена
    }

    @PostMapping(value = Endpoint.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        Logger.info("-------- User Register (" + registerRequestDto + ") --------");
        ResponseAuthDto responseAuthDto = registerService.registerUser(registerRequestDto);  // регистрация и генерация токена
        Logger.info("User registered!");
        return ResponseEntity.ok(responseAuthDto);  // отправляем два токена
    }

    @PostMapping(value = Endpoint.UPDATE_ACCESS_TOKEN)
    public ResponseEntity<?> updateAccessToken(@Valid @RequestBody UpdateAccessTokenDto updateAccessTokenDto, HttpServletRequest request) {
        Logger.info("-------- Update access token (" + updateAccessTokenDto + ") --------");
        String accessToken = updateTokenService.updateAccessToken(updateAccessTokenDto, request);
        return ResponseEntity.ok(new UpdateAccessTokenDto(accessToken));
    }

    @PostMapping(value = Endpoint.LOGOUT)
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequestDto logoutRequestDto, HttpServletRequest request) {
        Logger.info("-------- Logout (" + logoutRequestDto + ") --------");
        logoutService.logoutUser(logoutRequestDto, request);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping(value = Endpoint.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        Logger.info("-------- Forgot password (" + forgotPasswordDto + ") --------");
        updatePasswordService.createPasswordResetToken(forgotPasswordDto.getEmail());
        return ResponseEntity.ok("Send reset password link successful");
    }

    @PostMapping(value = Endpoint.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        Logger.info("-------- Reset password (" + resetPasswordRequestDto + ") --------");
        updatePasswordService.resetPassword(resetPasswordRequestDto);
        return ResponseEntity.ok("Reset password successful");
    }
}
