package com.example.cinemate.dto.auth;

import com.example.cinemate.model.db.AuthProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUserDto {
    private String externalId;
    private String email;
    private String username;
    private String firstname;
    private String surname;
    private String picture;
    private AuthProvider provider;
    private String accessToken;
}
