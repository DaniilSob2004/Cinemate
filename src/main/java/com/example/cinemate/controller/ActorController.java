package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.actor.ActorDto;
import com.example.cinemate.dto.error.ErrorResponseDto;
import com.example.cinemate.exception.common.ContentAlreadyExists;
import com.example.cinemate.service.business.actor.ActorCrudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.ACTORS)
public class ActorController {

    private final ActorCrudService actorCrudService;

    public ActorController(ActorCrudService actorCrudService) {
        this.actorCrudService = actorCrudService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        List<ActorDto> actors = actorCrudService.getAll();
        Logger.info("Successfully retrieved " + actors.size() + " actors");
        return ResponseEntity.ok(actors);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody ActorDto actorDto) {
        ErrorResponseDto errorResponseDto;
        try {
            actorCrudService.add(actorDto);
            return ResponseEntity.ok("Actor added successfully");
        } catch (ContentAlreadyExists e) {
            errorResponseDto = new ErrorResponseDto(e.getMessage(), HttpStatus.CONFLICT.value());
        }
        return ResponseEntity.status(errorResponseDto.getStatus()).body(errorResponseDto);
    }
}
