package com.example.cinemate.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GrantedAuthorityMapper {

    public List<String> toStringList(final Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public List<GrantedAuthority> toGrantedAuthorities(final List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public List<String> toListRolesString(final Object claimRoles) {
        List<String> roles = new ArrayList<>();
        if (claimRoles instanceof List<?> listClaimRoles) {
            roles = listClaimRoles.stream()
                    .filter(elem -> elem instanceof String)  // фильтруем элементы типа String
                    .map(String.class::cast)  // безопасное приведение Object к String
                    .toList();
        }
        return roles;
    }
}
