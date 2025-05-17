package com.example.cinemate.dao.contentwarning;

import com.example.cinemate.model.db.ContentWarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentWarningRepository extends JpaRepository<ContentWarning, Integer> {

    @Query("Select cw.warning.id from ContentWarning cw where cw.content.id = ?1")
    List<Integer> getIdWarnings(Integer contentId);

    @Query("SELECT cw FROM ContentWarning cw WHERE cw.content.id IN :ids")
    List<ContentWarning> findAllByContentIds(@Param("ids") List<Integer> ids);

    void deleteByContentIdAndWarningId(Integer contentId, Integer warningId);
}
