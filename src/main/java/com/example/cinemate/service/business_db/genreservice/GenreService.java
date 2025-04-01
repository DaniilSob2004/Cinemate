package com.example.cinemate.service.business_db.genreservice;

import com.example.cinemate.model.db.Genre;

import java.util.List;

public interface GenreService {
    void save(Genre genre);
    void saveGenresList(List<Genre> genres);
    void update(Genre genre);
    void delete(Genre genre);
    List<Genre> findAll();
    void deleteAll();
}
