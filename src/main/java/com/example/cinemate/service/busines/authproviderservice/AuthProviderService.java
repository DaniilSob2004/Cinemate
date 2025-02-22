package com.example.cinemate.service.busines.authproviderservice;

import com.example.cinemate.model.db.AuthProvider;

import java.util.List;
import java.util.Optional;

public interface AuthProviderService {
    void save(AuthProvider authProvider);
    void saveAuthProvidersList(List<AuthProvider> authProviders);
    void update(AuthProvider authProvider);
    void delete(AuthProvider authProvider);
    List<AuthProvider> findAll();
    void deleteAll();

    Optional<AuthProvider> findByName(String name);
}
