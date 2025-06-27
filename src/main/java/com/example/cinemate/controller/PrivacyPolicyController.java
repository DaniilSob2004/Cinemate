package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = Endpoint.API_V1)
@Tag(name = "Privacy Policy", description = "API for managing privacy policy")
public class PrivacyPolicyController {

    @GetMapping(value = Endpoint.PRIVACY_POLICY)
    @Operation(summary = "Get privacy policy text", description = "Returns the current privacy policy text of the application")
    public ResponseEntity<String> getPrivacyPolicy() {
        String privacyPolicyText = "My text privacy policy...";
        return ResponseEntity.ok(privacyPolicyText);
    }
}
