package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.CONTENT_VIEWS)
public class ContentViewHistoryController {

    @GetMapping(value = Endpoint.BY_USER_ID)
    public ResponseEntity<?> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok("User id: " + userId);
    }
}
