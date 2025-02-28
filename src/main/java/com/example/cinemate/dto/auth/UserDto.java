package com.example.cinemate.dto.auth;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String firstname;
    private String surname;
    private String email;
    private String phoneNum;
    private String avatar;
    private String provider;
}
