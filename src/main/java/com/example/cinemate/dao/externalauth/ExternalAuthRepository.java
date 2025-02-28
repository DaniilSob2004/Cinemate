package com.example.cinemate.dao.externalauth;

import com.example.cinemate.model.db.ExternalAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExternalAuthRepository extends JpaRepository<ExternalAuth, Integer> {
    Optional<ExternalAuth> findExternalAuthByUserId(Integer userId);

    @Query("SELECT COUNT(e) > 0 FROM ExternalAuth e WHERE e.provider.name = :providerName AND e.externalId = :externalId")
    boolean existsByProviderNameAndExternalId(
            @Param("providerName") String providerName,
            @Param("externalId") String externalId
    );
}
