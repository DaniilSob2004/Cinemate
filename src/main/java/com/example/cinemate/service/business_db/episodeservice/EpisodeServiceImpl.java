package com.example.cinemate.service.business_db.episodeservice;

import com.example.cinemate.dao.episode.EpisodeRepository;
import com.example.cinemate.model.db.Episode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeServiceImpl implements EpisodeService {

    private final EpisodeRepository episodeRepository;

    public EpisodeServiceImpl(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
    }

    @Override
    public void save(Episode episode) {
        episodeRepository.save(episode);
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
    public List<Episode> getByContentId(Integer contentId) {
        return episodeRepository.getEpisodesByContentId(contentId);
    }
}
