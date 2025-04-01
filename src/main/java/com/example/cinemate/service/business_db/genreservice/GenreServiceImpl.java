package com.example.cinemate.service.business_db.genreservice;

import com.example.cinemate.dao.genre.GenreRepository;
import com.example.cinemate.model.db.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public void save(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void saveGenresList(List<Genre> genres) {
        genreRepository.saveAll(genres);
    }

    @Override
    public void update(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void delete(Genre genre) {
        genreRepository.delete(genre);
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public void deleteAll() {
        genreRepository.deleteAll();
    }
}
