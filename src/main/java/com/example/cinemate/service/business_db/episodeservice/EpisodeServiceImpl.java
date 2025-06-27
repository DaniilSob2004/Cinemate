package com.example.cinemate.service.business_db.episodeservice;

import com.example.cinemate.dao.episode.EpisodeRepository;
import com.example.cinemate.model.db.Episode;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "episodes")
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;

    public EpisodeServiceImpl(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Override
    public Episode save(Episode episode) {
        return episodeRepository.save(episode);
    }

    @Override
    public void saveEpisodesList(List<Episode> episodes) {
        episodeRepository.saveAll(episodes);
    }

    @Override
    public void update(Episode episode) {
        episodeRepository.save(episode);
    }

    @Override
    public void delete(Episode episode) {
        episodeRepository.delete(episode);
    }

    @Override
    public List<Episode> findAll() {
        return episodeRepository.findAll();
    }

    @Override
    public void deleteAll() {
        episodeRepository.deleteAll();
    }

    @Override
    public Optional<Episode> findById(Integer id) {
        return episodeRepository.findById(id);
    }

    @Override
    public List<Episode> getByContentId(Integer contentId) {
        return episodeRepository.getEpisodesByContentId(contentId);
    }

    @Override
    public boolean existsByContentId(Integer contentId, int seasonNumber, int episodeNumber) {
        return episodeRepository.existsEpisodeByContentIdAndSeasonNumberAndEpisodeNumber(contentId, seasonNumber, episodeNumber);
    }

    @Override
    public Optional<Integer> getIdByContentId(Integer contentId, int seasonNumber, int episodeNumber) {
        return episodeRepository.getIdByContentIdAndSeasonNumberAndEpisodeNumber(contentId, seasonNumber, episodeNumber);
    }
}
