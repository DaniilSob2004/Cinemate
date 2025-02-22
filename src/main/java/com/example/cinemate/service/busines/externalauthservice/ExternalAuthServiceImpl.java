package com.example.cinemate.service.busines.externalauthservice;

import com.example.cinemate.dao.externalauth.ExternalAuthRepository;
import com.example.cinemate.model.db.ExternalAuth;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalAuthServiceImpl implements ExternalAuthService {

    private final ExternalAuthRepository externalAuthRepository;

    public ExternalAuthServiceImpl(ExternalAuthRepository externalAuthRepository) {
        this.externalAuthRepository = externalAuthRepository;
    }

    @Override
    public void save(ExternalAuth externalAuth) {
        externalAuthRepository.save(externalAuth);
    }

    @Override
    public void saveExternalAuthsList(List<ExternalAuth> externalAuths) {
        externalAuthRepository.saveAll(externalAuths);
    }

    @Override
    public void update(ExternalAuth externalAuth) {
        externalAuthRepository.save(externalAuth);
    }

    @Override
    public void delete(ExternalAuth externalAuth) {
        externalAuthRepository.delete(externalAuth);
    }

    @Override
    public List<ExternalAuth> findAll() {
        return externalAuthRepository.findAll();
    }

    @Override
    public void deleteAll() {
        externalAuthRepository.deleteAll();
    }
}
