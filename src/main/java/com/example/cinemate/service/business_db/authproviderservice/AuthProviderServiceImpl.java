package com.example.cinemate.service.business_db.authproviderservice;

import com.example.cinemate.dao.authprovider.AuthProviderRepository;
import com.example.cinemate.model.db.AuthProvider;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "provider")
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
    @CachePut(key = "#authProvider.name")
    public void update(AuthProvider authProvider) {
        authProviderRepository.save(authProvider);
    }

    @Override
    @CacheEvict(key = "#authProvider.name")
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
    @Cacheable(key = "#name")
    public Optional<AuthProvider> findByName(String name) {
        return authProviderRepository.findAuthProviderByName(name);
    }
}
