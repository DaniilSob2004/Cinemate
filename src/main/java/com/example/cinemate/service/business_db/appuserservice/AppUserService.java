package com.example.cinemate.service.business_db.appuserservice;

import com.example.cinemate.dto.user.UserSearchParamsDto;
import com.example.cinemate.model.db.AppUser;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    AppUser save(AppUser user);
    void saveUsersList(List<AppUser> users);
    AppUser update(AppUser user);
    void delete(AppUser user);
    List<AppUser> findAll();
    void deleteAll();

    Page<AppUser> getUsers(UserSearchParamsDto userSearchParamsDto);
    boolean existsByEmail(String email);
    Optional<AppUser> findByIdWithoutIsActive(Integer id);
    Optional<AppUser> findByEmailWithoutIsActive(String email);
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);
}
