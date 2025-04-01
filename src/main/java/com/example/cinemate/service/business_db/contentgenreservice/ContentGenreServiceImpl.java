package com.example.cinemate.service.business_db.contentgenreservice;

import com.example.cinemate.dao.contentgenre.ContentGenreRepository;
import com.example.cinemate.model.db.ContentGenre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentGenreServiceImpl implements ContentGenreService {

    private final ContentGenreRepository contentGenreRepository;

    public ContentGenreServiceImpl(ContentGenreRepository contentGenreRepository) {
        this.contentGenreRepository = contentGenreRepository;
    }

    @Override
    public void save(ContentGenre contentGenre) {
        contentGenreRepository.save(contentGenre);
    }

    @Override
    public void saveContentGenresList(List<ContentGenre> contentGenres) {
        contentGenreRepository.saveAll(contentGenres);
    }

    @Override
    public void update(ContentGenre contentGenre) {
        contentGenreRepository.save(contentGenre);
    }

    @Override
    public void delete(ContentGenre contentGenre) {
        contentGenreRepository.delete(contentGenre);
    }

    @Override
    public List<ContentGenre> findAll() {
        return contentGenreRepository.findAll();
    }

    @Override
    public void deleteAll() {
        contentGenreRepository.deleteAll();
    }
}
