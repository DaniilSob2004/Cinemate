package com.example.cinemate.service.business_db.roleservice;

import com.example.cinemate.dao.role.RoleRepository;
import com.example.cinemate.model.db.Role;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
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
    public void update(Role role) {
        roleRepository.save(role);
    }

    @Override
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
    public Optional<Role> findRoleByName(String name) {
        return roleRepository.findRoleByName(name);
    }
}
