package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.role.RoleDto;
import com.example.cinemate.service.business.role.RoleCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ROLES)
@SecurityRequirement(name = "JWT")
@Tag(name = "Role Admin", description = "Administration of user roles")
public class RoleController {

    private final RoleCrudService roleCrudService;

    public RoleController(RoleCrudService roleCrudService) {
        this.roleCrudService = roleCrudService;
    }

    @GetMapping
    @Operation(summary = "Get all roles", description = "Retrieves the list(RoleDto) of all available user roles")
    public ResponseEntity<?> getAll() {
        List<RoleDto> roles = roleCrudService.getAll();
        Logger.info("Successfully retrieved {} roles", roles.size());
        return ResponseEntity.ok(roles);
    }
}
