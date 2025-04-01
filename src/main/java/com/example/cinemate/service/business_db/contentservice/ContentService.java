package com.example.cinemate.service.business_db.contentservice;

import com.example.cinemate.model.db.Content;

import java.util.List;

public interface ContentService {
    void save(Content content);
    void saveContentsList(List<Content> contents);
    void update(Content content);
    void delete(Content content);
    List<Content> findAll();
    void deleteAll();
}
