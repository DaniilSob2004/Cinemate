package com.example.cinemate.dto.auth;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {  // DTO для сериализации CustomUserDetails в кэш Redis
    private Integer id;  // добавили
    private String username;
    private String password;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;
    private List<String> authorities;
}
