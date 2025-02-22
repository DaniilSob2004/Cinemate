package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.mapper.AppUserMapper;
import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.busines.appuserservice.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserController {

    private final AppUserService appUserService;
    private final AppUserMapper appUserMapper;

    public UserController(AppUserService appUserService, AppUserMapper appUserMapper) {
        this.appUserService = appUserService;
        this.appUserMapper = appUserMapper;
    }

    // Для теста админа
    @GetMapping(value = Endpoint.GET_PHOTO)
    public ResponseEntity<?> getPhoto() {
        return ResponseEntity.ok("Your photo for admin!");
    }

    @GetMapping(value = Endpoint.USER_ID)
    public ResponseEntity<?> getCurrentUser(@PathVariable Integer id) {
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

    /*
    @GetMapping(value = Endpoint.GET_ME)
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = authService.tokenValidateFromHeader(request).orElse(null);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDto("Invalid or missing token", HttpStatus.UNAUTHORIZED.value()));
        }

        // id из токена
        Integer id = authService.getUserIdByToken(token).orElse(null);
        if (id == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponseDto("Invalid token", HttpStatus.UNAUTHORIZED.value()));
        }

        // получаем пользователя
        AppUser appUser = appUserService.findById(id).orElse(null);
        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponseDto("User with id '" + id + "' was not found", HttpStatus.NOT_FOUND.value()));
        }

        // преобразовываем в DTO
        UserDto userDto = appUserConvertDto.convertToUserDto(appUser);

        return ResponseEntity.ok(userDto);
    }*/
}
