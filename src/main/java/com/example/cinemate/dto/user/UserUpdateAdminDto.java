package com.example.cinemate.dto.user;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAdminDto {
    private String username;
    private String firstname;
    private String surname;
    private String email;
    private String phoneNum;
    private String password;
    private String avatar;
    private List<String> roles;
}
