package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.service.business.userservice.CurrentUserService;
import com.example.cinemate.service.business.userservice.UserCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserCrudService userCrudService;

    public UserController(CurrentUserService currentUserService, UserCrudService userCrudService) {
        this.currentUserService = currentUserService;
        this.userCrudService = userCrudService;
    }

    // Для теста админа
    @GetMapping(value = Endpoint.GET_PHOTO)
    public ResponseEntity<?> getPhoto() {
        return ResponseEntity.ok("Your photo for admin!");
    }

    @GetMapping(value = Endpoint.GET_BY_USER_ID)
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        ErrorResponseDto errorResponseDto;
        try {
            UserDto userDto =  userCrudService.getUserById(id);
            return ResponseEntity.ok(userDto);
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @GetMapping(value = Endpoint.ME)
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            UserDto userDto = currentUserService.getCurrentUser(request);
            return ResponseEntity.ok(userDto);
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PutMapping(value = Endpoint.ME)
    public ResponseEntity<?> updateUserData(@RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        ErrorResponseDto errorResponseDto;
        try {
            currentUserService.updateUser(userUpdateDto, request);
            return ResponseEntity.ok("User updated successfully");
        } catch (UnauthorizedException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }
}
