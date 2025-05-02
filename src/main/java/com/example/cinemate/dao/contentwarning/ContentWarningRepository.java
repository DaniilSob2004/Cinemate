package com.example.cinemate.dao.contentwarning;

import com.example.cinemate.model.db.ContentWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentWarningRepository extends JpaRepository<ContentWarning, Integer> {

    @Query("Select cw.warning.id from ContentWarning cw where cw.content.id = ?1")
    List<Integer> getIdWarnings(Integer contentId);

    void deleteByContentIdAndWarningId(Integer contentId, Integer warningId);
}
