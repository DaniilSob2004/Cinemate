package com.example.cinemate.service.business_db.contentviewhistoryservice;

import com.example.cinemate.model.db.ContentViewHistory;

import java.util.List;

public interface ContentViewHistoryService {
    void save(ContentViewHistory contentViewHistory);
    void saveContentViewHistories(List<ContentViewHistory> contentViewHistories);
    void update(ContentViewHistory contentViewHistory);
    void delete(ContentViewHistory contentViewHistory);
    List<ContentViewHistory> findAll();
    void deleteAll();
}
