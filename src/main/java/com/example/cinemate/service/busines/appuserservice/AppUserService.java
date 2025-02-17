package com.example.cinemate.service.busines.appuserservice;

import com.example.cinemate.model.db.AppUser;
import java.util.List;
import java.util.Optional;

public interface AppUserService {
    void save(AppUser user);
    int[] saveUsersList(List<AppUser> users);
    void update(AppUser user);
    void delete(AppUser user);
    List<AppUser> findAll();
    void deleteAll();

    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);
    Optional<List<AppUser>> findAllWithRoles();
    Optional<AppUser> findByEmailWithRoles(String email);
    Optional<AppUser> findByIdWithRoles(Integer id);
}
