package com.example.cinemate.service.business_db.genreservice;

import com.example.cinemate.model.db.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    void save(Genre genre);
    void saveGenresList(List<Genre> genres);
    void update(Genre genre);
    void delete(Genre genre);
    List<Genre> findAll();
    void deleteAll();

    Optional<Genre> findById(Integer id);
    Optional<Genre> findByName(String name);
}
