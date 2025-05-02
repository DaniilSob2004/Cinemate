package com.example.cinemate.dao.warning;

import com.example.cinemate.model.db.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Integer> {
    Optional<Warning> findWarningById(Integer id);
    Optional<Warning> findWarningByName(String name);
}
