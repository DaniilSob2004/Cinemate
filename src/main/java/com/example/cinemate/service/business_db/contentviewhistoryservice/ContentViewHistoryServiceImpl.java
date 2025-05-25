package com.example.cinemate.service.business_db.contentviewhistoryservice;

import com.example.cinemate.dao.contentviewhistory.ContentViewHistoryRepository;
import com.example.cinemate.model.db.ContentViewHistory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentViewHistoryServiceImpl implements ContentViewHistoryService {

    private final ContentViewHistoryRepository contentViewHistoryRepository;

    public ContentViewHistoryServiceImpl(ContentViewHistoryRepository contentViewHistoryRepository) {
        this.contentViewHistoryRepository = contentViewHistoryRepository;
    }

    @Override
    public void save(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.save(contentViewHistory);
    }

    @Override
    public void saveContentViewHistories(List<ContentViewHistory> contentViewHistories) {
        contentViewHistoryRepository.saveAll(contentViewHistories);
    }

    @Override
    public void update(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.save(contentViewHistory);
    }

    @Override
    public void delete(ContentViewHistory contentViewHistory) {
        contentViewHistoryRepository.delete(contentViewHistory);
    }

    @Override
    public List<ContentViewHistory> findAll() {
        return contentViewHistoryRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentViewHistoryRepository.deleteAll();
    }
}
