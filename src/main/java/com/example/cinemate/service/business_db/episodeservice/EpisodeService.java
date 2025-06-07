package com.example.cinemate.service.business_db.episodeservice;

import com.example.cinemate.model.db.Episode;

import java.util.List;
import java.util.Optional;

public interface EpisodeService {
    void save(Episode episode);
    void saveEpisodesList(List<Episode> episodes);
    void update(Episode episode);
    void delete(Episode episode);
    List<Episode> findAll();
    void deleteAll();

    Optional<Episode> findById(Integer id);
    List<Episode> getByContentId(Integer contentId);
    boolean existsByContentId(Integer contentId, int seasonNumber, int episodeNumber);
    Optional<Integer> getIdByContentId(Integer contentId, int seasonNumber, int episodeNumber);
}
