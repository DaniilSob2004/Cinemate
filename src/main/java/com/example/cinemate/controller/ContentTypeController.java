package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.contenttype.ContentTypeDto;
import com.example.cinemate.service.business.contenttype.ContentTypeCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENT_TYPES)
@Tag(name = "ContentType Admin", description = "ContentType management for admin")
public class ContentTypeController {

    private final ContentTypeCrudService contentTypeCrudService;

    public ContentTypeController(ContentTypeCrudService contentTypeCrudService) {
        this.contentTypeCrudService = contentTypeCrudService;
    }

    @GetMapping(value = Endpoint.ALL)
    @Operation(summary = "Get all contentTypes", description = "Get all contentTypes and return List(ContentTypeDto)")
    public ResponseEntity<?> getAll() {
        List<ContentTypeDto> contentTypes = contentTypeCrudService.getAll();
        Logger.info("Successfully retrieved " + contentTypes.size() + " content types");
        return ResponseEntity.ok(contentTypes);
    }

    @PostMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add contentType", description = "Add contentType by ContentTypeDto")
    public ResponseEntity<?> add(@Valid @RequestBody ContentTypeDto contentTypeDto) {
        Logger.info("-------- Add ContentType (" + contentTypeDto + ") --------");
        contentTypeCrudService.add(contentTypeDto);
        return ResponseEntity.ok("Content type added successfully");
    }
}
