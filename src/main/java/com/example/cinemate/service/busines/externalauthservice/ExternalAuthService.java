package com.example.cinemate.service.busines.externalauthservice;

import com.example.cinemate.model.AuthProvider;
import com.example.cinemate.model.ExternalAuth;
import java.util.List;
import java.util.Optional;

public interface ExternalAuthService {
    void save(ExternalAuth externalAuth);
    int[] saveExternalAuthsList(List<ExternalAuth> externalAuths);
    void update(ExternalAuth externalAuth);
    void delete(ExternalAuth externalAuth);
    List<ExternalAuth> findAll();
    void deleteAll();

    Optional<ExternalAuth> findByExternalId(String externalId);
    Optional<ExternalAuth> findByProviderAndExternalId(AuthProvider provider, String externalId);
}
