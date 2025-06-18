package com.example.cinemate.mapper.user;

import com.example.cinemate.dto.auth.*;
import com.example.cinemate.dto.user.UserAddDto;
import com.example.cinemate.dto.user.UserAdminDto;
import com.example.cinemate.dto.user.UserDto;
import com.example.cinemate.mapper.GrantedAuthorityMapper;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.amazon.AmazonS3Service;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class UserMapper {

    private final AmazonS3Service amazonS3Service;
    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public UserMapper(AmazonS3Service amazonS3Service, GrantedAuthorityMapper grantedAuthorityMapper) {
        this.amazonS3Service = amazonS3Service;
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

    public AppUser toAppUser(final RegisterRequestDto registerRequestDto) {
        return new AppUser(
                null,
                "",
                "",
                "",
                registerRequestDto.getEmail(),
                "",
                registerRequestDto.getPassword(),
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
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
                true
        );
    }

    public AppUser toAppUser(final UserAddDto userAddDto) {
        return new AppUser(
                null,
                userAddDto.getUsername(),
                userAddDto.getFirstname(),
                userAddDto.getSurname(),
                userAddDto.getEmail(),
                userAddDto.getPhoneNum(),
                userAddDto.getPassword(),
                "",
                LocalDateTime.now(),
                LocalDateTime.now(),
                userAddDto.isActive()
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
                amazonS3Service.getCloudFrontUrl(appUser.getAvatar()),
                provider
        );
    }

    public UserAdminDto toUserAdminDto(final AppUser appUser, final String provider, final List<String> roles) {
        return UserAdminDto.builder()
                .id(appUser.getId())
                .username(appUser.getUsername())
                .firstname(appUser.getFirstname())
                .surname(appUser.getSurname())
                .email(appUser.getEmail())
                .phoneNum(appUser.getPhoneNum())
                .avatar(amazonS3Service.getCloudFrontUrl(appUser.getAvatar()))
                .provider(provider)
                .roles(roles)
                .createdAt(appUser.getCreatedAt())
                .updatedAt(appUser.getUpdatedAt())
                .isActive(appUser.getIsActive())
                .build();
    }
}
