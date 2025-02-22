package com.example.cinemate.mapper;

import com.example.cinemate.dto.auth.UserDetailsDto;
import com.example.cinemate.model.CustomUserDetails;
import com.example.cinemate.model.db.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserDetailsMapper {

    private final GrantedAuthorityMapper grantedAuthorityMapper;

    public UserDetailsMapper(GrantedAuthorityMapper grantedAuthorityMapper) {
        this.grantedAuthorityMapper = grantedAuthorityMapper;
    }

    public UserDetailsDto toUserDetailsDto(final UserDetails userDetails) {
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            return new UserDetailsDto(
                    customUserDetails.getId(),
                    customUserDetails.getUsername(),
                    customUserDetails.getPassword(),
                    customUserDetails.isEnabled(),
                    customUserDetails.isAccountNonExpired(),
                    customUserDetails.isCredentialsNonExpired(),
                    customUserDetails.isAccountNonLocked(),
                    grantedAuthorityMapper.toStringList(userDetails.getAuthorities())
            );
        }
        return new UserDetailsDto();
    }

    public UserDetails toUserDetails(final UserDetailsDto userDetailsDto) {
        return new CustomUserDetails(
                userDetailsDto.getId(),
                userDetailsDto.getUsername(),
                userDetailsDto.getPassword(),
                grantedAuthorityMapper.toGrantedAuthorities(userDetailsDto.getAuthorities()),
                userDetailsDto.isEnabled(),
                userDetailsDto.isAccountNonExpired(),
                userDetailsDto.isCredentialsNonExpired(),
                userDetailsDto.isAccountNonLocked()
        );
    }

    public CustomUserDetails toCustomUserDetails(final AppUser user, final List<GrantedAuthority> grantList) {
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
