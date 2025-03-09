package com.example.cinemate.mapper;

import com.example.cinemate.dto.role.RoleDto;
import com.example.cinemate.model.db.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public RoleDto toRoleDto(final Role role) {
        return new RoleDto(
                role.getName()
        );
    }
}
