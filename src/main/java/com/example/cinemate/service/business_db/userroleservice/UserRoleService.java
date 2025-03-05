package com.example.cinemate.service.business_db.userroleservice;

import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    void save(UserRole userRole);
    void saveUserRolesList(List<UserRole> userRoles);
    void update(UserRole userRole);
    void delete(UserRole userRole);
    List<UserRole> findAll();
    void deleteAll();

    void saveByRoleName(AppUser user, String roleName);
    void deleteByRoleName(AppUser user, String roleName);
    Optional<UserRole> findByUserIdAndRoleId(Integer userId, Integer roleId);
    List<String> getRoleNames(Integer userId);
}
