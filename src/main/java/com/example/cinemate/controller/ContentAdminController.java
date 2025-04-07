package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.content.ContentFullAdminDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENTS)
public class ContentAdminController {

    @PutMapping
    public ResponseEntity<?> add(@RequestBody ContentFullAdminDto contentFullAdminDto) {
        return ResponseEntity.ok().body("Content: " + contentFullAdminDto);
    }
}
