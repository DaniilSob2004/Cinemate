package com.example.cinemate.dto.auth;

import com.example.cinemate.model.AuthProvider;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserAuthDto {
    String externalId;
    String email;
    String username;
    String firstname;
    String surname;
    AuthProvider provider;
}
