package com.example.cinemate.service.business_db.warningservice;

import com.example.cinemate.dao.warning.WarningRepository;
import com.example.cinemate.model.db.Warning;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarningServiceImpl implements WarningService {

    private final WarningRepository warningRepository;

    public WarningServiceImpl(WarningRepository warningRepository) {
        this.warningRepository = warningRepository;
    }

    @Override
    public void save(Warning userRole) {
        warningRepository.save(userRole);
    }

    @Override
    public void saveWarningsList(List<Warning> warnings) {
        warningRepository.saveAll(warnings);
    }

    @Override
    public void update(Warning userRole) {
        warningRepository.save(userRole);
    }

    @Override
    public void delete(Warning userRole) {
        warningRepository.delete(userRole);
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
    public Optional<Warning> findById(Integer id) {
        return warningRepository.findById(id);
    }

    @Override
    public Optional<Warning> findByName(String name) {
        return warningRepository.findWarningByName(name);
    }
}
