package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.service.business.content.ContentAdminCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.CONTENTS)
public class ContentAdminController {

    private final ContentAdminCrudService contentAdminCrudService;

    public ContentAdminController(ContentAdminCrudService contentAdminCrudService) {
        this.contentAdminCrudService = contentAdminCrudService;
    }

    @GetMapping
    public ResponseEntity<?> get(@Valid ContentSearchParamsDto contentSearchParamsDto) {
        ErrorResponseDto errorResponseDto;
        try {
            Logger.info(contentSearchParamsDto);
            return ResponseEntity.ok(contentAdminCrudService.getListContents(contentSearchParamsDto));
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @GetMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        ErrorResponseDto errorResponseDto;
        try {
            var contentFullAdminDto = contentAdminCrudService.getById(id);
            return ResponseEntity.ok(contentFullAdminDto);
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        ErrorResponseDto errorResponseDto;
        try {
            contentAdminCrudService.add(contentFullAdminDto);
            return ResponseEntity.ok("Content added successfully");
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        ErrorResponseDto errorResponseDto;
        try {
            contentAdminCrudService.updateById(id, contentFullAdminDto);
            return ResponseEntity.ok("Content updated successfully");
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        ErrorResponseDto errorResponseDto;
        try {
            contentAdminCrudService.delete(id);
            return ResponseEntity.ok("Content deleted successfully");
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            Logger.error(e.getMessage());
            errorResponseDto = new ErrorResponseDto("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
