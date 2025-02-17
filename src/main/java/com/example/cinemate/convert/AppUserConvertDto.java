package com.example.cinemate.convert;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.dto.auth.GoogleUserAuthDto;
import com.example.cinemate.dto.auth.RegisterRequestDto;
import com.example.cinemate.dto.auth.UserDto;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppUserConvertDto {

    public AppUserJwtDto convertToAppUserJwtDto(final UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            List<String> roles = customUserDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            return new AppUserJwtDto(
                    customUserDetails.getId(),
                    customUserDetails.getUsername(),
                    roles
            );
        }
        return new AppUserJwtDto();
    }

    public Map<String, Object> convertToClaimsJwt(final AppUserJwtDto appUserJwtDto) {
        if (appUserJwtDto == null) {
            return Map.of();
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("email", appUserJwtDto.getEmail());
        claims.put("roles", appUserJwtDto.getRoles());

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
                Integer.valueOf(claims.getSubject()),  // id
                claims.get("email", String.class),
                roles
        );
    }

    public AppUser convertToAppUser(final RegisterRequestDto registerRequestDto, final String password) {
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

    public AppUser convertToAppUser(final GoogleUserAuthDto googleUserAuthDto, final String password) {
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

    public UserDto convertToUserDto(final AppUser appUser) {
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
