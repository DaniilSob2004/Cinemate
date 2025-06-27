package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.authprovider.AuthProviderDto;
import com.example.cinemate.service.business.authprovider.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tinylog.Logger;

import java.util.List;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.PROVIDERS)
@Tag(name = "AuthProvider Admin", description = "Administration of authentication providers used for user sign-in")
public class AuthProviderController {

    private final ProviderService providerService;

    public AuthProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @GetMapping
    @Operation(summary = "Get all authProviders", description = "Retrieves the list(AuthProviderDto) of all available user authProviders")
    public ResponseEntity<?> getAll() {
        List<AuthProviderDto> providers = providerService.getAll();
        Logger.info("Successfully retrieved {} providers", providers.size());
        return ResponseEntity.ok(providers);
    }
}
