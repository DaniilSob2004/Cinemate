package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.mapper.user.UserFileMapper;
import com.example.cinemate.service.business.common.UploadFilesAsyncService;
import com.example.cinemate.service.business.user.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USER)
@SecurityRequirement(name = "JWT")
@Tag(name = "User", description = "User management")
public class UserController {

    private final CurrentUserService currentUserService;
    private final UploadFilesAsyncService uploadFilesAsyncService;
    private final UserFileMapper userFileMapper;
    private final CommonMapper commonMapper;

    public UserController(CurrentUserService currentUserService, UploadFilesAsyncService uploadFilesAsyncService, UserFileMapper userFileMapper, CommonMapper commonMapper) {
        this.currentUserService = currentUserService;
        this.uploadFilesAsyncService = uploadFilesAsyncService;
        this.userFileMapper = userFileMapper;
        this.commonMapper = commonMapper;
    }

    @GetMapping(value = Endpoint.ME)
    @Operation(summary = "Get current user", description = "Get current user by jwt-token and return UserDto")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Logger.info("Get current user");
        return ResponseEntity.ok(currentUserService.getUser(request));
    }

    @PutMapping(value = Endpoint.ME, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update current user", description = "Update current user by jwt-token")
    public ResponseEntity<?> updateCurrentUser(
            @RequestPart(value = "metadata")
            @Parameter(description = "User metadata JSON (UserUpdateDto) in string format: " + """
                    {
                        "username": "@dan",
                        "firstname": "Daniil",
                        "surname": "Sobolev",
                        "email": "dansob@gmail.com",
                        "phoneNum": "+380682354332"
                    }""")
            String metadataStr,
            @RequestPart(value = "avatar") @Parameter(description = "User avatar image file") MultipartFile avatar,
            HttpServletRequest request
    ) throws IOException {

        // получение dto и проверка валидности
        var userUpdateDto = commonMapper.toDtoAndValidation(metadataStr, UserUpdateDto.class);

        var userFilesDto = new UserFilesDto(avatar);
        var userFilesBufferDto = userFileMapper.toUserFilesBufferDto(userFilesDto);
        Logger.info("-------- Update user (" + userUpdateDto + ") " + " (" + userFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем аватарку (если она есть)
        var updatedUser = currentUserService.updateUser(userUpdateDto, request);
        uploadFilesAsyncService.uploadUserFilesAndUpdate(updatedUser, userFilesBufferDto);

        return ResponseEntity.ok("User updated successfully. Avatar are loading...");
    }
}
