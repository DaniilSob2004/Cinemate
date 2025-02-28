package com.example.cinemate.service.business_db.roleservice;

import com.example.cinemate.dao.role.RoleRepository;
import com.example.cinemate.model.db.Role;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "role")
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void saveRolesList(List<Role> roles) {
        roleRepository.saveAll(roles);
    }

    @Override
    @CachePut(key = "#role.name")
    public void update(Role role) {
        roleRepository.save(role);
    }

    @Override
    @CacheEvict(key = "#role.name")
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void deleteAll() {
        roleRepository.deleteAll();
    }

    @Override
    @Cacheable(key = "#name")
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}
