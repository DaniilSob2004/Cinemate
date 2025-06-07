package com.example.cinemate.controller;

import com.example.cinemate.config.Endpoint;
import com.example.cinemate.dto.user.*;
import com.example.cinemate.service.business.user.CrudUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tinylog.Logger;

import javax.validation.Valid;

@RestController
@RequestMapping(value = Endpoint.API_V1 + Endpoint.USERS)
public class UserAdminController {

    private final CrudUserService crudUserService;

    public UserAdminController(CrudUserService crudUserService) {
        this.crudUserService = crudUserService;
    }

    @GetMapping
    public ResponseEntity<?> get(@Valid UserSearchParamsDto userSearchParamsDto) {
        Logger.info("-------- Get users (" + userSearchParamsDto + ") --------");
        return ResponseEntity.ok(crudUserService.getUsers(userSearchParamsDto));
    }

    @PostMapping(value = Endpoint.ADD_USER)
    public ResponseEntity<?> add(@Valid @RequestBody UserAddDto userAddDto) {
        Logger.info("-------- Add user (" + userAddDto + ") --------");
        return ResponseEntity.ok("User added successfully");
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
