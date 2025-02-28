package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.exception.auth.UnauthorizedException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business.userservice.CurrentUserService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserController {

    private final AppUserService appUserService;
    private final CurrentUserService currentUserService;
    private final AppUserMapper appUserMapper;

    public UserController(AppUserService appUserService, CurrentUserService currentUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.currentUserService = currentUserService;
        this.appUserMapper = appUserMapper;
    }

    // Для теста админа
    @GetMapping(value = Endpoint.GET_PHOTO)
    public ResponseEntity<?> getPhoto() {
        return ResponseEntity.ok("Your photo for admin!");
    }

    @GetMapping(value = Endpoint.USER_ID)
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        // получаем пользователя по id
        AppUser appUser = appUserService.findById(id).orElse(null);
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("User with id '" + id + "' was not found", HttpStatus.NOT_FOUND.value()));
        }

        // преобразовываем в DTO
        UserDto userDto = appUserMapper.toUserDto(appUser);

        return ResponseEntity.ok(userDto);
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
}
