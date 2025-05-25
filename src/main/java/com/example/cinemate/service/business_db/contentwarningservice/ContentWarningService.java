package com.example.cinemate.service.business_db.contentwarningservice;

import com.example.cinemate.model.db.ContentWarning;

import java.util.List;
import java.util.Map;

public interface ContentWarningService {
    void save(ContentWarning contentWarning);
    void saveContentWarningsList(List<ContentWarning> contentWarnings);
    void update(ContentWarning contentWarning);
    void delete(ContentWarning contentWarning);
    List<ContentWarning> findAll();
    void deleteAll();

    List<Integer> getIdWarnings(Integer contentId);
    void deleteByContentIdAndWarningId(Integer contentId, Integer warningId);

    Map<Integer, List<Integer>> getWarningsByContentIds(List<Integer> contentIds);
}
