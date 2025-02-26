package com.example.cinemate.service.business_db.userroleservice;

import com.example.cinemate.model.db.UserRole;
import java.util.List;

public interface UserRoleService {
    void save(UserRole userRole);
    void saveUserRolesList(List<UserRole> userRoles);
    void update(UserRole userRole);
    void delete(UserRole userRole);
    List<UserRole> findAll();
    void deleteAll();

    List<String> getRoleNames(Integer userId);
}
