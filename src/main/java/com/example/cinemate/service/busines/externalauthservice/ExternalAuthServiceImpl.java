package com.example.cinemate.service.busines.externalauthservice;

import com.example.cinemate.dao.externalauth.ExternalAuthRepository;
import com.example.cinemate.model.ExternalAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ExternalAuthServiceImpl implements ExternalAuthService {

    @Autowired
    private ExternalAuthRepository externalAuthRepository;

    @Override
    public void save(ExternalAuth externalAuth) {
        externalAuthRepository.save(externalAuth);
    }

    @Override
    public int[] saveExternalAuthsList(List<ExternalAuth> externalAuths) {
        externalAuthRepository.saveAll(externalAuths);
        return new int[0];
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
