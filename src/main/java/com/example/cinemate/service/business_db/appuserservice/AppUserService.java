package com.example.cinemate.service.business_db.appuserservice;

import com.example.cinemate.model.db.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    void save(AppUser user);
    void saveUsersList(List<AppUser> users);
    void update(AppUser user);
    void delete(AppUser user);
    List<AppUser> findAll();
    void deleteAll();

    boolean existsByEmail(String email);
    Optional<AppUser> findByIdWithoutIsActive(Integer id);
    Optional<AppUser> findByEmailWithoutIsActive(String email);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);
}
