package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.genre.*;
import com.example.cinemate.dto.genre.file.GenreFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.service.business.genre.GenreCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.GENRES)
@Tag(name = "Genre", description = "Genre management for contents and users test")
public class GenreController {

    private final GenreCrudService genreCrudService;
    private final CommonMapper commonMapper;

    public GenreController(GenreCrudService genreCrudService, CommonMapper commonMapper) {
        this.genreCrudService = genreCrudService;
        this.commonMapper = commonMapper;
    }

    @GetMapping(value = Endpoint.ALL)
    @Operation(summary = "Get all genres", description = "Get all genres and return List(GenreDto)")
    public ResponseEntity<?> getAll() {
        List<GenreDto> genres = genreCrudService.getAll();
        Logger.info("Successfully retrieved " + genres.size() + " genres");
        return ResponseEntity.ok(genres);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Add genre", description = "Add genre with metadata and image")
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata")
            @Parameter(description = "Genre metadata JSON (GenreDto) in string format: " + """
                    {
                        "name": "Horror",
                        "description": "Any description",
                        "tags": "#tag1,#tag2"
                    }""")
            String metadataStr,
            @RequestPart(value = "image") @Parameter(description = "Image representing the genre") MultipartFile image
    ) throws IOException {

        // получение dto и проверка валидности
        var genreDto = commonMapper.toDtoAndValidation(metadataStr, GenreDto.class);
        var genreFilesDto = new GenreFilesDto(image);
        Logger.info("-------- Add genre for admin (" + genreDto + ") " + " (" + genreFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем картинку
        genreCrudService.add(genreDto, genreFilesDto);

        return ResponseEntity.ok("Genre added successfully. Image are loading...");
    }

    @PostMapping(value = Endpoint.BY_RECOMMENDATIONS_TEST)
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Set test user genres", description = "Set test genres to check the recommendations of content for the user")
    public ResponseEntity<?> addGenresTest(@RequestBody GenreRecTestDto genreRecTestDto, HttpServletRequest request){
        Logger.info("-------- Add genre for user test (" + genreRecTestDto + ") --------");
        genreCrudService.addGenresTest(genreRecTestDto, request);
        return ResponseEntity.ok("Genres for user test added successfully");
    }

    @GetMapping(value = "get-genre-test")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get test user genres", description = "Get test genres to check the recommendations of content for the user")
    public ResponseEntity<?> getGenreTest(HttpServletRequest request) {
        return ResponseEntity.ok(genreCrudService.getGenreTest(request));
    }
}
