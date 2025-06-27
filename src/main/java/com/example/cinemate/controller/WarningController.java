package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.warning.WarningDto;
import com.example.cinemate.service.business.warning.WarningCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.WARNINGS)
@SecurityRequirement(name = "JWT")
@Tag(name = "Warning Admin", description = "Warning management for admin")
public class WarningController {

    private final WarningCrudService warningCrudService;

    public WarningController(WarningCrudService warningCrudService) {
        this.warningCrudService = warningCrudService;
    }

    @GetMapping
    @Operation(summary = "Get all warnings", description = "Get all warnings and return List(WarningDto)")
    public ResponseEntity<?> getAll() {
        List<WarningDto> warnings = warningCrudService.getAll();
        Logger.info("Successfully retrieved " + warnings.size() + " warnings");
        return ResponseEntity.ok(warnings);
    }

    @PostMapping
    @Operation(summary = "Add warning", description = "Add warning by WarningDto")
    public ResponseEntity<?> add(@Valid @RequestBody WarningDto warningDto) {
        Logger.info("-------- Add warning (" + warningDto + ") --------");
        warningCrudService.add(warningDto);
        return ResponseEntity.ok("Warning added successfully");
    }
}
