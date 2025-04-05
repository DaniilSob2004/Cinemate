package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.dto.warning.WarningDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.service.business.warning.WarningCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.WARNINGS)
public class WarningController {

    private final WarningCrudService warningCrudService;

    public WarningController(WarningCrudService warningCrudService) {
        this.warningCrudService = warningCrudService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<WarningDto> warnings = warningCrudService.getAll();
            Logger.info("Successfully retrieved " + warnings.size() + " warnings");
            return ResponseEntity.ok(warnings);
        } catch (Exception e) {
            Logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value())
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody WarningDto warningDto) {
        ErrorResponseDto errorResponseDto;
        try {
            warningCrudService.add(warningDto);
            return ResponseEntity.ok("Warning added successfully");
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);  // отправка ошибки
    }
}
