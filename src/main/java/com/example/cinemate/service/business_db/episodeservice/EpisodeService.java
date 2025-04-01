package com.example.cinemate.service.business_db.episodeservice;

import com.example.cinemate.model.db.Episode;

import java.util.List;

public interface EpisodeService {
    void save(Episode episode);
    void saveEpisodesList(List<Episode> episodes);
    void update(Episode episode);
    void delete(Episode episode);
    List<Episode> findAll();
    void deleteAll();
}
