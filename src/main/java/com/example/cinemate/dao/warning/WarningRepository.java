package com.example.cinemate.dao.warning;

import com.example.cinemate.model.db.Warning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningRepository extends JpaRepository<Warning, Integer> {

}
