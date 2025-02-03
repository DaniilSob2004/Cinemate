package com.example.cinemate.convert;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.model.AppUser;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppUserConvertDto {
    public AppUserJwtDto convertToAppUserJwtDto(final AppUser appUser) {
        if (appUser == null) {
            return new AppUserJwtDto();
        }

        List<String> roleNames = appUser.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .toList();

        return new AppUserJwtDto(
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getSurname(),
                roleNames,
                appUser.getEmail(),
                appUser.getPhoneNum(),
                appUser.getAvatar()
        );
    }

    public Map<String, Object> convertToClaimsJwt(final AppUserJwtDto appUserJwtDto) {
        if (appUserJwtDto == null) {
            return Map.of();
        }

        Map<String, Object> claims = new HashMap<>();

        claims.put("username", appUserJwtDto.getUsername());
        claims.put("firstname", appUserJwtDto.getFirstname());
        claims.put("surname", appUserJwtDto.getSurname());
        claims.put("roles", appUserJwtDto.getRoles());
        claims.put("phoneNum", appUserJwtDto.getPhoneNum());
        claims.put("avatar", appUserJwtDto.getAvatar());

        return claims;
    }

    public AppUserJwtDto convertToAppUserJwtDto(final Claims claims) {
        if (claims == null) {
            return new AppUserJwtDto();
        }

        // список ролей
        Object claimRoles = claims.get("roles");
        List<String> roles = claimRoles != null ? (List<String>) claimRoles : new ArrayList<>();

        return new AppUserJwtDto(
                claims.get("username", String.class),
                claims.get("firstname", String.class),
                claims.get("surname", String.class),
                roles,
                claims.getSubject(),  // email
                claims.get("phoneNum", String.class),
                claims.get("avatar", String.class)
        );
    }
}
