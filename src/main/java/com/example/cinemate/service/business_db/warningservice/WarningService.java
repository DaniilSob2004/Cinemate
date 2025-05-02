package com.example.cinemate.service.business_db.warningservice;

import com.example.cinemate.model.db.Warning;

import java.util.List;
import java.util.Optional;

public interface WarningService {
    void save(Warning warning);
    void saveWarningsList(List<Warning> warnings);
    void update(Warning warning);
    void delete(Warning warning);
    List<Warning> findAll();
    void deleteAll();

    Optional<Warning> findById(Integer id);
    Optional<Warning> findByName(String name);
}
