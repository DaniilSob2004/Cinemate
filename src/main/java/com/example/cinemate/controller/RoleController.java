package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.dto.role.RoleDto;
import com.example.cinemate.service.business.role.RoleCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ROLES)
public class RoleController {

    private final RoleCrudService roleCrudService;

    public RoleController(RoleCrudService roleCrudService) {
        this.roleCrudService = roleCrudService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<RoleDto> roles = roleCrudService.getAll();
            Logger.info("Successfully retrieved {} roles", roles.size());
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }
}
