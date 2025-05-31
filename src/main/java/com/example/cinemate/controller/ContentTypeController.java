package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.contenttype.ContentTypeDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.service.business.contenttype.ContentTypeCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENT_TYPES)
public class ContentTypeController {

    private final ContentTypeCrudService contentTypeCrudService;

    public ContentTypeController(ContentTypeCrudService contentTypeCrudService) {
        this.contentTypeCrudService = contentTypeCrudService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ContentTypeDto> contentTypes = contentTypeCrudService.getAll();
        Logger.info("Successfully retrieved " + contentTypes.size() + " content types");
        return ResponseEntity.ok(contentTypes);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ContentTypeDto contentTypeDto) {
        ErrorResponseDto errorResponseDto;
        try {
            contentTypeCrudService.add(contentTypeDto);
            return ResponseEntity.ok("Content type added successfully");
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
