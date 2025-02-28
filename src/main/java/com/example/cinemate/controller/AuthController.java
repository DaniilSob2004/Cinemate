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
import com.example.cinemate.service.redis.BlacklistTokenRedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.AUTH)
public class AuthController {

    private final AuthService authService;
    private final LoginService loginService;
    private final RegisterService registerService;
    private final BlacklistTokenRedisService blacklistTokenRedisService;

    public AuthController(AuthService authService, LoginService loginService, RegisterService registerService, BlacklistTokenRedisService blacklistTokenRedisService) {
        this.authService = authService;
        this.loginService = loginService;
        this.registerService = registerService;
        this.blacklistTokenRedisService = blacklistTokenRedisService;
    }

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto("Invalid Basic Authentication", HttpStatus.BAD_REQUEST.value()));
        }

        // аутентификация и генерация токена
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info("-------- User Login (" + loginRequestDto.getEmail() + ") --------");

            token = loginService.loginUser(loginRequestDto);

            Logger.info("User authenticated!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (BadCredentialsException | InternalAuthenticationServiceException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        }  catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
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
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info("-------- User Register (" + registerRequestDto + ") --------");

            token = registerService.registerUser(registerRequestDto);

            Logger.info("User registered!");

            return ResponseEntity.ok(new AuthResponseDto(token));  // отправка токена

        } catch (UserAlreadyExistsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (PasswordMismatchException | InvalidEmailException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (BadCredentialsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (Exception e) {
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }

        Logger.info("-------- Token in logout controller (" + token + ") --------");

        // добавление токена в 'Redis' blacklist (чтобы до истечение срока нельзя было его использ.)
        blacklistTokenRedisService.addToBlacklist(token);

        return ResponseEntity.ok("Logout successful");
    }
}
