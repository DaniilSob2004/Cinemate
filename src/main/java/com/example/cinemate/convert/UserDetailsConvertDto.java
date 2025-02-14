package com.example.cinemate.convert;

import com.example.cinemate.dto.auth.UserDetailsDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsConvertDto {

    public UserDetailsDto convertToUserDetailsDto(final UserDetails userDetails) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return new UserDetailsDto(
                userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.isEnabled(),
                userDetails.isAccountNonExpired(),
                userDetails.isCredentialsNonExpired(),
                userDetails.isAccountNonLocked(),
                authorities
        );
    }

    public UserDetails convertToUserDetails(final UserDetailsDto userDetailsDto) {
        var grantedAuthorities = userDetailsDto.getAuthorities().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new User(
                userDetailsDto.getUsername(),
                userDetailsDto.getPassword(),
                userDetailsDto.isEnabled(),
                userDetailsDto.isAccountNonExpired(),
                userDetailsDto.isCredentialsNonExpired(),
                userDetailsDto.isAccountNonLocked(),
                grantedAuthorities
        );
    }
}
