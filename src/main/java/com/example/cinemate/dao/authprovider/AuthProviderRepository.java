package com.example.cinemate.dao.authprovider;

import com.example.cinemate.model.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Integer> {
    Optional<AuthProvider> findAuthProviderByName(String name);
}
