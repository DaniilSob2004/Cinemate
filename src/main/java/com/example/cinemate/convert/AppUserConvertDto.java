package com.example.cinemate.convert;

import com.example.cinemate.dto.auth.AppUserJwtDto;
import com.example.cinemate.model.AppUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppUserConvertDto {
    public AppUserJwtDto convertToAppUserJwtDto(final AppUser appUser) {
        if (appUser == null) {
            return new AppUserJwtDto();
        }

        List<String> roleNames = appUser.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());

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
}
