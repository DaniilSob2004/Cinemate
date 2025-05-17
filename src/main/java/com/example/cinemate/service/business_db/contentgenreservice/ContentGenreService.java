package com.example.cinemate.service.business_db.contentgenreservice;

import com.example.cinemate.model.db.ContentGenre;

import java.util.List;
import java.util.Map;

public interface ContentGenreService {
    void save(ContentGenre contentGenre);
    void saveContentGenresList(List<ContentGenre> contentGenres);
    void update(ContentGenre contentGenre);
    void delete(ContentGenre contentGenre);
    List<ContentGenre> findAll();
    void deleteAll();

    List<Integer> getIdGenres(Integer contentId);
    List<ContentGenre> findAllByContentIds(List<Integer> ids);
    void deleteByContentIdAndGenreId(Integer contentId, Integer genreId);

    Map<Integer, List<Integer>> getGenresByContentIds(List<Integer> contentIds);
}
