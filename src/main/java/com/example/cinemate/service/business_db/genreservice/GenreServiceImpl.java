package com.example.cinemate.service.business_db.genreservice;

import com.example.cinemate.dao.genre.GenreRepository;
import com.example.cinemate.model.db.Genre;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "genre")
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
    @CachePut(key = "#genre.id")
    public void update(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    @CacheEvict(key = "#genre.id")
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

    @Override
    @Cacheable(key = "#id")
    public Optional<Genre> findById(Integer id) {
        return genreRepository.findById(id);
    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findGenreByName(name);
    }
}
