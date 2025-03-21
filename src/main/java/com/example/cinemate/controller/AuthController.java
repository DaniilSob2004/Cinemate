package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.auth.*;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.auth.*;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.exception.common.EmailSendException;
import com.example.cinemate.service.auth.LoginService;
import com.example.cinemate.service.auth.LogoutService;
import com.example.cinemate.service.auth.RegisterService;
import com.example.cinemate.service.auth.UpdateTokenService;
import com.example.cinemate.service.business.user.UpdatePasswordService;
import com.example.cinemate.utils.HandleErrorUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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
        // аутентификация и генерация токена
        ErrorResponseDto errorResponseDto;
        try {
            ResponseAuthDto responseAuthDto = loginService.loginUser(request);
            Logger.info("User authenticated!");
            return ResponseEntity.ok(responseAuthDto);  // отправляем два токена

        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            errorResponseDto = HandleErrorUtil.handleUserInactiveException(e);  // обработка ошибки
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        // регистрация и генерация токена
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info("-------- User Register (" + registerRequestDto + ") --------");
            ResponseAuthDto responseAuthDto = registerService.registerUser(registerRequestDto);
            Logger.info("User registered!");
            return ResponseEntity.ok(responseAuthDto);  // отправляем два токена

        } catch (UserAlreadyExistsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (BadCredentialsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.UPDATE_ACCESS_TOKEN)
    public ResponseEntity<?> updateAccessToken(@Valid @RequestBody UpdateAccessTokenDto updateAccessTokenDto, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            String accessToken = updateTokenService.updateAccessToken(updateAccessTokenDto, request);
            return ResponseEntity.ok(new UpdateAccessTokenDto(accessToken));
        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.LOGOUT)
    public ResponseEntity<?> logout(@Valid @RequestBody LogoutRequestDto logoutRequestDto, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            logoutService.logoutUser(logoutRequestDto, request);
            return ResponseEntity.ok("Logout successful");
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.FORGOT_PASSWORD)
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
        ErrorResponseDto errorResponseDto;
        try {
            updatePasswordService.createPasswordResetToken(forgotPasswordDto.getEmail());
            return ResponseEntity.ok("Send reset password link successful");
        } catch (UserInactiveException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.LOCKED.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (EmailSendException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto) {
        ErrorResponseDto errorResponseDto;
        try {
            updatePasswordService.resetPassword(resetPasswordRequestDto);
            return ResponseEntity.ok("Reset password successful");
        } catch (UserInactiveException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.LOCKED.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }
}
