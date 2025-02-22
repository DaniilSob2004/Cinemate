package com.example.cinemate.service.busines.externalauthservice;

import com.example.cinemate.model.db.AuthProvider;
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
}
