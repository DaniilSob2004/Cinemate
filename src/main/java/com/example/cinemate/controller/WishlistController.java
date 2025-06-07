package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.wishlist.WishlistParamsDto;
import com.example.cinemate.service.business.wishlist.WishlistCrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.WISHLISTS)
public class WishlistController {

    private final WishlistCrudService wishlistCrudService;

    public WishlistController(WishlistCrudService wishlistCrudService) {
        this.wishlistCrudService = wishlistCrudService;
    }

    @GetMapping(value = Endpoint.ME)
    public ResponseEntity<?> getByUserId(@Valid @ModelAttribute WishlistParamsDto wishlistParamsDto, HttpServletRequest request) {
        Logger.info("-------- Get wishlists by current user (" + wishlistParamsDto + ") --------");
        return ResponseEntity.ok(wishlistCrudService.getByUserId(wishlistParamsDto, request));
    }
}
