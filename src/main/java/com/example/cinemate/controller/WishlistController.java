package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.wishlist.WishListAddDto;
import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.service.business.wishlist.WishlistCrudService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.WISHLISTS)
@SecurityRequirement(name = "JWT")
@Tag(name = "Wishlist", description = "Operations related to managing user wishlists")
public class WishlistController {

    private final WishlistCrudService wishlistCrudService;

    public WishlistController(WishlistCrudService wishlistCrudService) {
        this.wishlistCrudService = wishlistCrudService;
    }

    @GetMapping(value = Endpoint.ME)
    @Operation(summary = "Get user's wishlists", description = "Get user's wishlists based on filter and pagination parameters, returns PageResponse(WishlistDto)")
    public ResponseEntity<?> getByUserId(@Valid @ModelAttribute WishlistParamsDto wishlistParamsDto, HttpServletRequest request) {
        Logger.info("-------- Get wishlists by current user (" + wishlistParamsDto + ") --------");
        return ResponseEntity.ok(wishlistCrudService.getByUserId(wishlistParamsDto, request));
    }

    @PostMapping
    @Operation(summary = "Add wishlist", description = "Add a new wishlist for user")
    public ResponseEntity<?> add(@Valid @RequestBody WishListAddDto wishListAddDto, HttpServletRequest request) {
        Logger.info("-------- Add wishlist for user (" + wishListAddDto + ") --------");
        wishlistCrudService.add(wishListAddDto, request);
        return ResponseEntity.ok("Wishlist added successfully...");
    }
}
