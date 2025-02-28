package com.example.cinemate.service.business_db.userroleservice;

import com.example.cinemate.dao.userrole.UserRoleRepository;
import com.example.cinemate.model.db.UserRole;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "user::role")
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void save(UserRole userRole) {
        userRoleRepository.save(userRole);
    }

    @Override
    public void saveUserRolesList(List<UserRole> userRoles) {
        userRoleRepository.saveAll(userRoles);
    }

    @Override
    @CachePut(key = "#userRole.user.id")
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
    @Cacheable(key = "#userId")
    public List<String> getRoleNames(Integer userId) {
        return userRoleRepository.getRoleNames(userId);
    }
}
