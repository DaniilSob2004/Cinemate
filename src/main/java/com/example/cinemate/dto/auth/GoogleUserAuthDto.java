package com.example.cinemate.dto.auth;

import com.example.cinemate.model.db.AuthProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserAuthDto {
    private String externalId;
    private String email;
    private String username;
    private String firstname;
    private String surname;
    private AuthProvider provider;
}
