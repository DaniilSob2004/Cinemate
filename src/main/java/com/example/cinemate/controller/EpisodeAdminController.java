package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.exception.common.ContentNotFoundException;
import com.example.cinemate.service.business.episode.EpisodeCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ADMIN + Endpoint.EPISODES)
public class EpisodeAdminController {

    private final EpisodeCrudService episodeCrudService;

    public EpisodeAdminController(EpisodeCrudService episodeCrudService) {
        this.episodeCrudService = episodeCrudService;
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody EpisodeDto episodeDto) {
        ErrorResponseDto errorResponseDto;
        try {
            episodeCrudService.add(episodeDto);
            return ResponseEntity.ok("Episode added successfully");
        } catch (EntityNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody EpisodeDto EpisodeDto) {
        ErrorResponseDto errorResponseDto;
        try {
            episodeCrudService.updateById(id, EpisodeDto);
            return ResponseEntity.ok("Episode updated successfully");
        } catch (EntityNotFoundException | ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        ErrorResponseDto errorResponseDto;
        try {
            episodeCrudService.deleteById(id);
            return ResponseEntity.ok("Episode deleted successfully");
        } catch (ContentNotFoundException e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
