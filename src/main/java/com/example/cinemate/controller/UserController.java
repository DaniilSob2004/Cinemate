package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.service.business.user.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USER)
public class UserController {

    private final CurrentUserService currentUserService;

    public UserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping(value = Endpoint.ME)
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Logger.info("Get current user");
        UserDto userDto = currentUserService.getUser(request);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(value = Endpoint.ME)
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserUpdateDto userUpdateDto, HttpServletRequest request) {
        Logger.info("-------- Update current user (" + userUpdateDto + ") --------");
        currentUserService.updateUser(userUpdateDto, request);
        return ResponseEntity.ok("User updated successfully");
    }
}
