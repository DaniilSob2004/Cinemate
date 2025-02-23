package com.example.cinemate.service.business_db.authproviderservice;

import com.example.cinemate.dao.authprovider.AuthProviderRepository;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {

    private final AuthProviderRepository authProviderRepository;

    public AuthProviderServiceImpl(AuthProviderRepository authProviderRepository) {
        this.authProviderRepository = authProviderRepository;
    }

    @Override
    public void save(AuthProvider authProvider) {
        authProviderRepository.save(authProvider);
    }

    @Override
    public void saveAuthProvidersList(List<AuthProvider> authProviders) {
        authProviderRepository.saveAll(authProviders);
    }

    @Override
    public void update(AuthProvider authProvider) {
        authProviderRepository.save(authProvider);
    }

    @Override
    public void delete(AuthProvider authProvider) {
        authProviderRepository.delete(authProvider);
    }

    @Override
    public List<AuthProvider> findAll() {
        return authProviderRepository.findAll();
    }

    @Override
    public void deleteAll() {
        authProviderRepository.deleteAll();
    }


    @Override
    public Optional<AuthProvider> findByName(String name) {
        return authProviderRepository.findAuthProviderByName(name);
    }
}
