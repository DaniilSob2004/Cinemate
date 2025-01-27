package com.example.cinemate.service.busines.externalauthservice;

import com.example.cinemate.model.ExternalAuth;
import java.util.List;

public interface ExternalAuthService {
    void save(ExternalAuth externalAuth);
    int[] saveExternalAuthsList(List<ExternalAuth> externalAuths);
    void update(ExternalAuth externalAuth);
    void delete(ExternalAuth externalAuth);
    List<ExternalAuth> findAll();
    void deleteAll();
}
