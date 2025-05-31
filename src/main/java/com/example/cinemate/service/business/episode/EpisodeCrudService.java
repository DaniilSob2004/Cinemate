package com.example.cinemate.service.business.episode;

import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.mapper.EpisodeMapper;
import com.example.cinemate.service.business_db.episodeservice.EpisodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpisodeCrudService {

    private final EpisodeService episodeService;
    private final EpisodeMapper episodeMapper;

    public EpisodeCrudService(EpisodeService episodeService, EpisodeMapper episodeMapper) {
        this.episodeService = episodeService;
        this.episodeMapper = episodeMapper;
    }

    public List<EpisodeDto> getByContentId(final Integer contentId) {
        return episodeService.getByContentId(contentId).stream()
                .map(episodeMapper::toEpisodeDto)
                .toList();
    }
}
