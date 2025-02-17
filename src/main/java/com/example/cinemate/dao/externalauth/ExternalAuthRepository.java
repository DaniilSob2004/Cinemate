package com.example.cinemate.dao.externalauth;

import com.example.cinemate.model.db.AuthProvider;
import com.example.cinemate.model.db.ExternalAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExternalAuthRepository extends JpaRepository<ExternalAuth, Integer> {
    Optional<ExternalAuth> findExternalAuthByExternalId(String externalId);
    Optional<ExternalAuth> findExternalAuthByProviderAndExternalId(AuthProvider provider, String externalId);
}
