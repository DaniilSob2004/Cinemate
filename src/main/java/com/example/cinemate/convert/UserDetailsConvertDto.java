package com.example.cinemate.convert;

import com.example.cinemate.dto.auth.UserDetailsDto;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsConvertDto {

    public UserDetailsDto convertToUserDetailsDto(final UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            var authorities = customUserDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return new UserDetailsDto(
                    customUserDetails.getId(),
                    customUserDetails.getUsername(),
                    customUserDetails.getPassword(),
                    customUserDetails.isEnabled(),
                    customUserDetails.isAccountNonExpired(),
                    customUserDetails.isCredentialsNonExpired(),
                    customUserDetails.isAccountNonLocked(),
                    authorities
            );
        }
        return new UserDetailsDto();
    }

    public UserDetails convertToUserDetails(final UserDetailsDto userDetailsDto) {
        var grantedAuthorities = userDetailsDto.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new CustomUserDetails(
                userDetailsDto.getId(),
                userDetailsDto.getUsername(),
                userDetailsDto.getPassword(),
                grantedAuthorities,
                userDetailsDto.isEnabled(),
                userDetailsDto.isAccountNonExpired(),
                userDetailsDto.isCredentialsNonExpired(),
                userDetailsDto.isAccountNonLocked()
        );
    }

    public CustomUserDetails convertToCustomUserDetails(final AppUser user, final List<GrantedAuthority> grantList) {
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getEncPassword(),
                grantList,
                true,
                true,
                true,
                true
        );
    }
}
