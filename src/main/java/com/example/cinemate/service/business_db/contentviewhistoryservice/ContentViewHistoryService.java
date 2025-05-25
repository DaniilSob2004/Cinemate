package com.example.cinemate.service.business_db.contentviewhistoryservice;

import com.example.cinemate.dto.contentviewhistory.ContentHistoryParamsDto;
import com.example.cinemate.model.db.ContentViewHistory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContentViewHistoryService {
    void save(ContentViewHistory contentViewHistory);
    void saveContentViewHistories(List<ContentViewHistory> contentViewHistories);
    void update(ContentViewHistory contentViewHistory);
    void delete(ContentViewHistory contentViewHistory);
    List<ContentViewHistory> findAll();
    void deleteAll();

    Page<ContentViewHistory> getContentViewHistories(ContentHistoryParamsDto contentHistoryParamsDto);
    List<ContentViewHistory> findByUserId(int userId);
}
