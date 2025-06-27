package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.service.business.episode.EpisodeCrudService;
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
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.EPISODES)
@SecurityRequirement(name = "JWT")
@Tag(name = "Episode Admin", description = "Administration of episodes for content of type 'series'")
public class EpisodeAdminController {

    private final EpisodeCrudService episodeCrudService;
    private final CommonMapper commonMapper;

    public EpisodeAdminController(EpisodeCrudService episodeCrudService, CommonMapper commonMapper) {
        this.episodeCrudService = episodeCrudService;
        this.commonMapper = commonMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add episode", description = "Add a new episode with (EpisodeDto)")
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata")
            @Parameter(description = "Episode metadata JSON (EpisodeDto) in string format: " + """
                    {
                        "name": "New episode",
                        "contentId": 1,
                        "seasonNumber": 4,
                        "episodeNumber": 2,
                        "durationMin": 60,
                        "description": "NEW - Super episode (season 4 series 1)",
                        "releaseDate": "2022-07-23"
                    }""")
            String metadataStr,
            @RequestPart("trailer") @Parameter(description = "Trailer video file for the episode") MultipartFile trailer,
            @RequestPart("video") @Parameter(description = "Main video file for the episode") MultipartFile video
    ) throws IOException {

        // получение dto и проверка валидности
        var episodeDto = commonMapper.toDtoAndValidation(metadataStr, EpisodeDto.class);

        //Logger.info("-------- Add episode (" + episodeDto + ") --------");
        //episodeCrudService.add(episodeDto);
        return ResponseEntity.ok("Episode added successfully");
    }

    @PutMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Update episode by id", description = "Update episode information by their id")
    public ResponseEntity<?> updateById(
            @PathVariable @Parameter(description = "Episode id", example = "757") Integer id,
            @Valid @RequestBody EpisodeDto EpisodeDto) {
        Logger.info("-------- Update episode for admin by id: " + id + " - (" + EpisodeDto + ") --------");
        episodeCrudService.updateById(id, EpisodeDto);
        return ResponseEntity.ok("Episode updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Delete episode by id", description = "Delete episode by their id from database")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Logger.info("Delete episode for admin by id: " + id);
        episodeCrudService.deleteById(id);
        return ResponseEntity.ok("Episode deleted successfully");
    }
}
