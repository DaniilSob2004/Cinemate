package com.example.cinemate.service.business_db.warningservice;

import com.example.cinemate.dao.warning.WarningRepository;
import com.example.cinemate.model.db.Warning;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "warning")
public class WarningServiceImpl implements WarningService {

    private final WarningRepository warningRepository;

    public WarningServiceImpl(WarningRepository warningRepository) {
        this.warningRepository = warningRepository;
    }

    @Override
    public void save(Warning warning) {
        warningRepository.save(warning);
    }

    @Override
    public void saveWarningsList(List<Warning> warnings) {
        warningRepository.saveAll(warnings);
    }

    @Override
    @CachePut(key = "#warning.id")
    public void update(Warning warning) {
        warningRepository.save(warning);
    }

    @Override
    @CacheEvict(key = "#warning.id")
    public void delete(Warning warning) {
        warningRepository.delete(warning);
    }

    @Override
    public List<Warning> findAll() {
        return warningRepository.findAll();
    }

    @Override
    public void deleteAll() {
        warningRepository.deleteAll();
    }

    @Override
    @Cacheable(key = "#id")
    public Optional<Warning> findById(Integer id) {
        return warningRepository.findById(id);
    }

    @Override
    public Optional<Warning> findByName(String name) {
        return warningRepository.findWarningByName(name);
    }
}
