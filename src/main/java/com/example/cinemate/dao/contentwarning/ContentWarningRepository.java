package com.example.cinemate.dao.contentwarning;

import com.example.cinemate.model.db.ContentWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentWarningRepository extends JpaRepository<ContentWarning, Integer> {

}
