package com.example.cinemate.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String username;
    private String firstname;
    private String surname;
    private String email;
    private String phoneNum;
    private String avatar;
}
