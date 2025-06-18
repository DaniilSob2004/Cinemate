package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.UserUpdateDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.mapper.user.UserFileMapper;
import com.example.cinemate.service.business.user.CurrentUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USER)
public class UserController {

    private final CurrentUserService currentUserService;
    private final UserFileMapper userFileMapper;
    private final CommonMapper commonMapper;

    public UserController(CurrentUserService currentUserService, UserFileMapper userFileMapper, CommonMapper commonMapper) {
        this.currentUserService = currentUserService;
        this.userFileMapper = userFileMapper;
        this.commonMapper = commonMapper;
    }

    @GetMapping(value = Endpoint.ME)
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        Logger.info("Get current user");
        UserDto userDto = currentUserService.getUser(request);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(value = Endpoint.ME, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCurrentUser(
            @RequestPart(value = "metadata") String metadataStr,
            @RequestPart(value = "avatar") MultipartFile avatar,
            HttpServletRequest request
    ) throws IOException {

        // получение dto и проверка валидности
        var userUpdateDto = commonMapper.toDtoAndValidation(metadataStr, UserUpdateDto.class);

        var userFilesDto = new UserFilesDto(avatar);
        var userFilesBufferDto = userFileMapper.toUserFilesBufferDto(userFilesDto);
        Logger.info("-------- Update user (" + userUpdateDto + ") " + " (" + userFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем аватарку (если она есть)
        var updatedUser = currentUserService.updateUser(userUpdateDto, request);
        currentUserService.uploadFilesAndUpdate(updatedUser, userFilesBufferDto);

        return ResponseEntity.ok("User updated successfully. Avatar are loading...");
    }
}
