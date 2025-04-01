package com.example.cinemate.service.business.role;

import com.example.cinemate.dto.role.RoleDto;
import com.example.cinemate.mapper.RoleMapper;
import com.example.cinemate.service.business_db.roleservice.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleCrudService {

    private final RoleService roleService;
    private final RoleMapper roleMapper;

    public RoleCrudService(RoleService roleService, RoleMapper roleMapper) {
        this.roleService = roleService;
        this.roleMapper = roleMapper;
    }

    public List<RoleDto> getAll() {
        return roleService.findAll().stream()
                .map(roleMapper::toRoleDto)
                .toList();
    }
}
