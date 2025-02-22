package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.*;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class AppUserMapper {

    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public AppUserMapper(GrantedAuthorityMapper grantedAuthorityMapper) {
        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }

    public AppUserJwtDto toAppUserJwtDto(final UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            return new AppUserJwtDto(
                    customUserDetails.getId(),
                    customUserDetails.getUsername(),
                    grantedAuthorityMapper.toStringList(userDetails.getAuthorities())
            );
        }
        return new AppUserJwtDto();
    }

    public Map<String, Object> toClaimsJwt(final AppUserJwtDto appUserJwtDto) {
        if (appUserJwtDto == null) {
            return Map.of();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", appUserJwtDto.getEmail());
        claims.put("roles", appUserJwtDto.getRoles());

        return claims;
    }

    public AppUserJwtDto toAppUserJwtDto(final Claims claims) {
        if (claims == null) {
            return new AppUserJwtDto();
        }

        // список ролей
        Object claimRoles = claims.get("roles");
        List<String> roles = new ArrayList<>();
        if (claimRoles instanceof List<?> listClaimRoles) {
            roles = listClaimRoles.stream()
                    .filter(elem -> elem instanceof String)  // фильтруем элементы типа String
                    .map(String.class::cast)  // безопасное приведение Object к String
                    .toList();
        }

        return new AppUserJwtDto(
                Integer.valueOf(claims.getSubject()),  // id
                claims.get("email", String.class),
                roles
        );
    }

    public AppUser toAppUser(final RegisterRequestDto registerRequestDto, final String password) {
        return new AppUser(
                null,
                "",
                "",
                "",
                registerRequestDto.getEmail(),
                "",
                password,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public AppUser toAppUser(final GoogleUserAuthDto googleUserAuthDto, final String password) {
        return new AppUser(
                null,
                googleUserAuthDto.getUsername(),
                googleUserAuthDto.getFirstname(),
                googleUserAuthDto.getSurname(),
                googleUserAuthDto.getEmail(),
                "",
                password,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                null
        );
    }

    public UserDto toUserDto(final AppUser appUser) {
        return new UserDto(
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getSurname(),
                appUser.getEmail(),
                appUser.getPhoneNum(),
                appUser.getAvatar()
        );
    }
}
