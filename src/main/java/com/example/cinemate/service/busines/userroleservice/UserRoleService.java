package com.example.cinemate.service.busines.userroleservice;

import com.example.cinemate.model.UserRole;
import java.util.List;

public interface UserRoleService {
    void save(UserRole userRole);
    int[] saveUserRolesList(List<UserRole> userRoles);
    void update(UserRole userRole);
    void delete(UserRole userRole);
    List<UserRole> findAll();
    void deleteAll();

    List<String> getRoleNames(Integer userId);
}
