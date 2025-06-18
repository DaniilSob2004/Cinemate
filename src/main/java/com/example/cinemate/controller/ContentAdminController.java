package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.file.ContentFilesDto;
import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.mapper.content.ContentFileMapper;
import com.example.cinemate.service.business.content.ContentAdminCrudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.CONTENTS)
public class ContentAdminController {

    private final ContentAdminCrudService contentAdminCrudService;
    private final ContentFileMapper contentFileMapper;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public ContentAdminController(ContentAdminCrudService contentAdminCrudService, ContentFileMapper contentFileMapper, ObjectMapper objectMapper, Validator validator) {
        this.contentAdminCrudService = contentAdminCrudService;
        this.contentFileMapper = contentFileMapper;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @GetMapping
    public ResponseEntity<?> get(@Valid ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get contents for admin (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentAdminCrudService.getListContents(contentSearchParamsDto));
    }

    @GetMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Logger.info("Get content for admin by id: " + id);
        var contentFullAdminDto = contentAdminCrudService.getById(id);
        return ResponseEntity.ok(contentFullAdminDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata") String metadataStr,
            @RequestPart(value = "poster") MultipartFile poster,
            @RequestPart(value = "trailer") MultipartFile trailer,
            @RequestPart(value = "video") MultipartFile video
    ) throws IOException {

        // получение dto и проверка валидности
        var contentFullAdminDto = objectMapper.readValue(metadataStr, ContentFullAdminDto.class);
        Set<ConstraintViolation<ContentFullAdminDto>> violations = validator.validate(contentFullAdminDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        var contentFilesDto = new ContentFilesDto(poster, trailer, video);
        var contentFilesBufferDto = contentFileMapper.toContentFilesBufferDto(contentFilesDto);

        Logger.info("-------- Add content for admin (" + contentFullAdminDto + ") " + " (" + contentFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем трейлер видео
        var savedContent = contentAdminCrudService.addInitial(contentFullAdminDto, contentFilesBufferDto);
        contentAdminCrudService.uploadFilesAndUpdate(savedContent, contentFilesBufferDto);

        return ResponseEntity.ok("Content added successfully. Videos are loading...");
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        Logger.info("-------- Update content for admin by id: " + id + " - (" + contentFullAdminDto + ") --------");
        contentAdminCrudService.updateById(id, contentFullAdminDto);
        return ResponseEntity.ok("Content updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        Logger.info("Delete content for admin by id: " + id);
        contentAdminCrudService.delete(id);
        return ResponseEntity.ok("Content deleted successfully");
    }
}
