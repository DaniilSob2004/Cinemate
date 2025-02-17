package com.example.cinemate.service.busines.authproviderservice;

import com.example.cinemate.dao.authprovider.AuthProviderRepository;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthProviderServiceImpl implements AuthProviderService {

    @Autowired
    private AuthProviderRepository authProviderRepository;

    @Override
    public void save(AuthProvider authProvider) {
        authProviderRepository.save(authProvider);
    }

    @Override
    public int[] saveAuthProvidersList(List<AuthProvider> authProviders) {
        authProviderRepository.saveAll(authProviders);
        return new int[0];
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
