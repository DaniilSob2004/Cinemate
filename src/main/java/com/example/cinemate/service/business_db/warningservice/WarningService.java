package com.example.cinemate.service.business_db.warningservice;

import com.example.cinemate.model.db.Warning;

import java.util.List;
import java.util.Optional;

public interface WarningService {
    void save(Warning userRole);
    void saveWarningsList(List<Warning> warnings);
    void update(Warning userRole);
    void delete(Warning userRole);
    List<Warning> findAll();
    void deleteAll();

    Optional<Warning> findByName(String name);
}
