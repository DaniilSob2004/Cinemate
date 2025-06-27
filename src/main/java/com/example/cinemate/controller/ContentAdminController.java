package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.file.ContentFilesDto;
import com.example.cinemate.dto.content.ContentFullAdminDto;
import com.example.cinemate.dto.content.ContentSearchParamsDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.service.business.content.ContentAdminCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.CONTENTS)
@SecurityRequirement(name = "JWT")
@Tag(name = "Content Admin", description = "Content management for admin")
public class ContentAdminController {

    private final ContentAdminCrudService contentAdminCrudService;
    private final CommonMapper commonMapper;

    public ContentAdminController(ContentAdminCrudService contentAdminCrudService, CommonMapper commonMapper) {
        this.contentAdminCrudService = contentAdminCrudService;
        this.commonMapper = commonMapper;
    }

    @GetMapping
    @Operation(summary = "Get contents", description = "Get contents and return PageResponse(ContentListAdminDto)")
    public ResponseEntity<?> get(@Valid ContentSearchParamsDto contentSearchParamsDto) {
        Logger.info("-------- Get contents for admin (" + contentSearchParamsDto + ") --------");
        return ResponseEntity.ok(contentAdminCrudService.getListContents(contentSearchParamsDto));
    }

    @GetMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Get content by id", description = "Get content by id and return ContentFullAdminDto")
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "Content id", example = "7") Integer id) {
        Logger.info("Get content for admin by id: " + id);
        return ResponseEntity.ok(contentAdminCrudService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add content", description = "Add a new content with metadata, poster and videos")
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata")
            @Parameter(description = "Content metadata JSON (ContentFullAdminDto) in string format: " + """
                    {
                        "name": "Dune 2",
                        "contentType": "movie",
                        "description": "Description film",
                        "durationMin": 120,
                        "ageRating": "16+",
                        "releaseDate": "2020-12-01",
                        "actors": [1, 5],
                        "genres": [2, 10],
                        "warnings": [1],
                        "isActive": true
                    }""")
            String metadataStr,
            @RequestPart(value = "poster") @Parameter(description = "Poster image file for the content") MultipartFile poster,
            @RequestPart(value = "trailer") @Parameter(description = "Trailer video file for the content") MultipartFile trailer,
            @RequestPart(value = "video") @Parameter(description = "Main video file for the content") MultipartFile video
    ) throws IOException {

        // получение dto и проверка валидности
        var contentFullAdminDto = commonMapper.toDtoAndValidation(metadataStr, ContentFullAdminDto.class);
        var contentFilesDto = new ContentFilesDto(poster, trailer, video);
        Logger.info("-------- Add content for admin (" + contentFullAdminDto + ") " + " (" + contentFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем трейлер видео
        contentAdminCrudService.add(contentFullAdminDto, contentFilesDto);

        return ResponseEntity.ok("Content added successfully. Videos are loading...");
    }

    @PutMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Update content by id", description = "Update content information by their id")
    public ResponseEntity<?> updateById(
            @PathVariable @Parameter(description = "Content id", example = "7") Integer id,
            @Valid @RequestBody ContentFullAdminDto contentFullAdminDto) {
        Logger.info("-------- Update content for admin by id: " + id + " - (" + contentFullAdminDto + ") --------");
        contentAdminCrudService.updateById(id, contentFullAdminDto);
        return ResponseEntity.ok("Content updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Delete content by id", description = "Delete content by their id from database")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "Content id", example = "7")  Integer id) {
        Logger.info("Delete content for admin by id: " + id);
        contentAdminCrudService.delete(id);
        return ResponseEntity.ok("Content deleted successfully");
    }
}
