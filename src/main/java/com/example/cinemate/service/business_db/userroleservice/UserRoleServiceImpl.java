package com.example.cinemate.service.business_db.userroleservice;

import com.example.cinemate.dao.userrole.UserRoleRepository;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.model.db.Role;
import com.example.cinemate.model.db.UserRole;
import com.example.cinemate.service.business_db.roleservice.RoleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "user::role")
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, RoleService roleService) {
        this.userRoleRepository = userRoleRepository;
        this.roleService = roleService;
    }

    @Override
    @CachePut(key = "#userRole.user.id")
    public void save(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    public void saveUserRolesList(List<UserRole> userRoles) {
        userRoleRepository.saveAll(userRoles);
    }

    @Override
    @CacheEvict(key = "#userRole.user.id")
    public void update(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    @CacheEvict(key = "#userRole.user.id")
    public void delete(UserRole userRole) {
        userRoleRepository.delete(userRole);
    }

    @Override
    public List<UserRole> findAll() {
        return userRoleRepository.findAll();
    }

    @Override
    public void deleteAll() {
        userRoleRepository.deleteAll();
    }

    @Override
    @CacheEvict(key = "#user.id")
    public void saveByRoleName(AppUser user, String roleName) {
        roleService.findRoleByName(roleName).ifPresent(role -> this.save(
                new UserRole(null, user, role)
        ));
    }

    @Override
    @CacheEvict(key = "#user.id")
    public void deleteByRoleName(AppUser user, String roleName) {
        roleService.findRoleByName(roleName)
                .flatMap(role -> this.findByUserIdAndRoleId(user.getId(), role.getId()))
                .ifPresent(this::delete);
    }

    @Override
    public Optional<UserRole> findByUserIdAndRoleId(Integer userId, Integer roleId) {
        return userRoleRepository.findUserRoleByUserIdAndRoleId(userId, roleId);
    }

    @Override
    @Cacheable(key = "#userId")
    public List<String> getRoleNames(Integer userId) {
        return userRoleRepository.getRoleNames(userId);
    }
}
