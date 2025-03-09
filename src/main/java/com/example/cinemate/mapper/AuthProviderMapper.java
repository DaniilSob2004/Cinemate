package com.example.cinemate.mapper;

import com.example.cinemate.dto.authprovider.AuthProviderDto;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.stereotype.Component;

@Component
public class AuthProviderMapper {

    public AuthProviderDto toAuthProviderDto(final AuthProvider authProvider) {
        return new AuthProviderDto(
                authProvider.getName()
        );
    }
}
