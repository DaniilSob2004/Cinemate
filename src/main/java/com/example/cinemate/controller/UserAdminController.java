package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.*;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.service.business.user.CrudUserService;
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
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
@SecurityRequirement(name = "JWT")
@Tag(name = "User Admin", description = "User management for admin")
public class UserAdminController {

    private final CrudUserService crudUserService;
    private final CommonMapper commonMapper;

    public UserAdminController(CrudUserService crudUserService, CommonMapper commonMapper) {
        this.crudUserService = crudUserService;
        this.commonMapper = commonMapper;
    }

    @GetMapping
    @Operation(summary = "Get users", description = "Get users and return PageResponse(UserAdminDto)")
    public ResponseEntity<?> get(@Valid UserSearchParamsDto userSearchParamsDto) {
        Logger.info("-------- Get users (" + userSearchParamsDto + ") --------");
        return ResponseEntity.ok(crudUserService.getUsers(userSearchParamsDto));
    }

    @PostMapping(value = Endpoint.ADD_USER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Add user", description = "Add a new user with metadata and avatar image")
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata")
            @Parameter(description = "User metadata JSON (UserAddDto) in string format: " + """
                    {
                        "username": "@dan",
                        "firstname": "Daniil",
                        "surname": "Sobolev",
                        "email": "dansob@gmail.com",
                        "password": "12345",
                        "phoneNum": "+380682354332",
                        "roles": ["ROLE_USER"],
                        "isActive": true
                    }""")
            String metadataStr,
            @RequestPart(value = "avatar") @Parameter(description = "User avatar image file") MultipartFile avatar
    ) throws IOException {

        // получение dto и проверка валидности
        var userAddDto = commonMapper.toDtoAndValidation(metadataStr, UserAddDto.class);
        var userFilesDto = new UserFilesDto(avatar);
        Logger.info("-------- Add user for admin (" + userAddDto + ") " + " (" + userFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем аватарку
        crudUserService.add(userAddDto, userFilesDto);

        return ResponseEntity.ok("User added successfully. Avatar are loading...");
    }

    @GetMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Get user by id", description = "Get user by id and return UserAdminDto")
    public ResponseEntity<?> getById(@PathVariable @Parameter(description = "User id", example = "5") Integer id) {
        Logger.info("Get user for admin by id: " + id);
        return ResponseEntity.ok(crudUserService.getById(id));
    }

    @PutMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Update user by id", description = "Update user information by their id")
    public ResponseEntity<?> updateById(
            @PathVariable @Parameter(description = "User id", example = "5") Integer id,
            @Valid @RequestBody UserUpdateAdminDto userUpdateAdminDto) {
        Logger.info("-------- Update user for admin by id: " + id + " - (" + userUpdateAdminDto + ") --------");
        crudUserService.updateById(id, userUpdateAdminDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    @Operation(summary = "Delete user by id", description = "Delete user by their id, making a user inactive")
    public ResponseEntity<?> deleteById(@PathVariable @Parameter(description = "User id", example = "5") Integer id) {
        Logger.info("Delete user for admin by id: " + id);
        crudUserService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
