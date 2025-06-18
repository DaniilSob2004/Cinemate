package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.*;
import com.example.cinemate.dto.user.file.UserFilesDto;
import com.example.cinemate.mapper.common.CommonMapper;
import com.example.cinemate.mapper.user.UserFileMapper;
import com.example.cinemate.service.business.user.CrudUserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tinylog.Logger;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserAdminController {

    private final CrudUserService crudUserService;
    private final UserFileMapper userFileMapper;
    private final CommonMapper commonMapper;

    public UserAdminController(CrudUserService crudUserService, UserFileMapper userFileMapper, CommonMapper commonMapper) {
        this.crudUserService = crudUserService;
        this.userFileMapper = userFileMapper;
        this.commonMapper = commonMapper;
    }

    @GetMapping
    public ResponseEntity<?> get(@Valid UserSearchParamsDto userSearchParamsDto) {
        Logger.info("-------- Get users (" + userSearchParamsDto + ") --------");
        return ResponseEntity.ok(crudUserService.getUsers(userSearchParamsDto));
    }

    @PostMapping(value = Endpoint.ADD_USER, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> add(
            @RequestPart(value = "metadata") String metadataStr,
            @RequestPart(value = "avatar") MultipartFile avatar
    ) throws IOException {

        // получение dto и проверка валидности
        var userAddDto = commonMapper.toDtoAndValidation(metadataStr, UserAddDto.class);

        var userFilesDto = new UserFilesDto(avatar);
        var userFilesBufferDto = userFileMapper.toUserFilesBufferDto(userFilesDto);
        Logger.info("-------- Add user for admin (" + userAddDto + ") " + " (" + userFilesDto + ") --------");

        // сохраняем в БД и в отдельном потоке загружаем аватарку
        var savedUser = crudUserService.add(userAddDto);
        crudUserService.uploadFilesAndUpdate(savedUser, userFilesBufferDto);

        return ResponseEntity.ok("User added successfully. Avatar are loading...");
    }

    @GetMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Logger.info("Get user for admin by id: " + id);
        UserDto userDto =  crudUserService.getById(id);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> updateById(@PathVariable Integer id, @Valid @RequestBody UserUpdateAdminDto userUpdateAdminDto) {
        Logger.info("-------- Update user for admin by id: " + id + " - (" + userUpdateAdminDto + ") --------");
        crudUserService.updateById(id, userUpdateAdminDto);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping(value = Endpoint.BY_ID)
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        Logger.info("Delete user for admin by id: " + id);
        crudUserService.delete(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
