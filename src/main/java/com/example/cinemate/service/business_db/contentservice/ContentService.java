package com.example.cinemate.service.business_db.contentservice;

import com.example.cinemate.model.db.Content;

import java.util.List;
import java.util.Optional;

public interface ContentService {
    Content save(Content content);
    void saveContentsList(List<Content> contents);
    void update(Content content);
    void delete(Content content);
    List<Content> findAll();
    void deleteAll();

    Optional<Content> findById(Integer id);
    Optional<Content> findByName(String name);
}
