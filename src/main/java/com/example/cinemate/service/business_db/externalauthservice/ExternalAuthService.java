package com.example.cinemate.service.business_db.externalauthservice;

import com.example.cinemate.model.db.ExternalAuth;

import java.util.List;
import java.util.Optional;

public interface ExternalAuthService {
    void save(ExternalAuth externalAuth);
    void saveExternalAuthsList(List<ExternalAuth> externalAuths);
    void update(ExternalAuth externalAuth);
    void delete(ExternalAuth externalAuth);
    List<ExternalAuth> findAll();
    void deleteAll();

    Optional<ExternalAuth> findByUserId(Integer userId);
    Optional<ExternalAuth> findByProviderNameAndExternalId(String providerName, String externalId);
}
