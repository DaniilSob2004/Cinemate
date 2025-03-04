package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.*;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
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

    public AppUserJwtDto toAppUserJwtDto(final UserDetails userDetails, final String provider) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            return new AppUserJwtDto(
                    customUserDetails.getId(),
                    grantedAuthorityMapper.toStringList(userDetails.getAuthorities()),
                    provider
            );
        }
        return new AppUserJwtDto();
    }

    public Map<String, Object> toClaimsJwt(final AppUserJwtDto appUserJwtDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", appUserJwtDto.getRoles());
        claims.put("provider", appUserJwtDto.getProvider());
        return claims;
    }

    public Map<String, Object> toClaimsJwt(final RefreshTokenDto refreshTokenDto) {
        Map<String, Object> claims = new HashMap<>();
        return claims;
    }

    public AppUserJwtDto toAppUserJwtDto(@NonNull final Claims claims) {
        return new AppUserJwtDto(
                Integer.valueOf(claims.getSubject()),  // id
                grantedAuthorityMapper.toListRolesString(claims.get("roles")),  // список ролей
                claims.get("provider", String.class)
        );
    }

    public RefreshTokenDto toRefreshTokenDto(@NonNull final Claims claims) {
        return new RefreshTokenDto(
                Integer.valueOf(claims.getSubject())  // id
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
                true,
                null
        );
    }

    public AppUser toAppUser(final OAuthUserDto oAuthUserDto) {
        return new AppUser(
                null,
                oAuthUserDto.getUsername(),
                oAuthUserDto.getFirstname(),
                oAuthUserDto.getSurname(),
                oAuthUserDto.getEmail(),
                "",
                null,
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null
        );
    }

    public UserDto toUserDto(final AppUser appUser, final String provider) {
        return new UserDto(
                appUser.getId(),
                appUser.getUsername(),
                appUser.getFirstname(),
                appUser.getSurname(),
                appUser.getEmail(),
                appUser.getPhoneNum(),
                appUser.getAvatar(),
                provider
        );
    }
}
