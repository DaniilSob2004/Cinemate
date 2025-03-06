package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.dto.user.UserAddDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.user.UserUpdateAdminDto;
import com.example.cinemate.exception.auth.UserAlreadyExistsException;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.service.business.userservice.UserCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserAdminController {

    private final UserCrudService userCrudService;

    public UserAdminController(UserCrudService userCrudService) {
        this.userCrudService = userCrudService;
    }

    @GetMapping(value = Endpoint.BY_USER_ID)
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        ErrorResponseDto errorResponseDto;
        try {
            UserDto userDto =  userCrudService.getById(id);
            return ResponseEntity.ok(userDto);
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PutMapping(value = Endpoint.BY_USER_ID)
    public ResponseEntity<?> updateUserById(@PathVariable Integer id, @Valid @RequestBody UserUpdateAdminDto userUpdateAdminDto) {
        ErrorResponseDto errorResponseDto;
        try {
            userCrudService.updateById(id, userUpdateAdminDto);
            return ResponseEntity.ok("User updated successfully");
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (UserAlreadyExistsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }

    @PostMapping(value = Endpoint.ADD_USER)
    public ResponseEntity<?> addUser(@Valid @RequestBody UserAddDto userAddDto) {
        ErrorResponseDto errorResponseDto;
        try {
            userCrudService.add(userAddDto);
            return ResponseEntity.ok("User added successfully");
        } catch (UserNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (UserAlreadyExistsException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (BadRequestException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }
}
