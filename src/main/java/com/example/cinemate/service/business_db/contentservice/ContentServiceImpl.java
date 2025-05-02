package com.example.cinemate.service.business_db.contentservice;

import com.example.cinemate.dao.content.ContentRepository;
import com.example.cinemate.model.db.Content;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    public ContentServiceImpl(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Content save(Content content) {
        return contentRepository.save(content);
    }

    @Override
    public void saveContentsList(List<Content> contents) {
        contentRepository.saveAll(contents);
    }

    @Override
    public void update(Content content) {
        contentRepository.save(content);
    }

    @Override
    public void delete(Content content) {
        contentRepository.delete(content);
    }

    @Override
    public List<Content> findAll() {
        return contentRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentRepository.deleteAll();
    }

    @Override
    public Optional<Content> findById(Integer id) {
        return contentRepository.findContentById(id);
    }

    @Override
    public Optional<Content> findByName(String name) {
        return contentRepository.findContentByNameIgnoreCase(name);
    }
}
