package com.example.cinemate.service.business.authprovider;

import com.example.cinemate.dto.authprovider.AuthProviderDto;
import com.example.cinemate.mapper.AuthProviderMapper;
import com.example.cinemate.service.business_db.authproviderservice.AuthProviderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProviderService {

    private final AuthProviderService authProviderService;
    private final AuthProviderMapper authProviderMapper;

    public ProviderService(AuthProviderService authProviderService, AuthProviderMapper authProviderMapper) {
        this.authProviderService = authProviderService;
        this.authProviderMapper = authProviderMapper;
    }

    public List<AuthProviderDto> getAll() {
        return authProviderService.findAll().stream()
                .map(authProviderMapper::toAuthProviderDto)
                .toList();
    }
}
