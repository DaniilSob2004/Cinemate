package com.example.cinemate.mapper;

import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.model.db.Episode;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

@Component
public class EpisodeMapper {

    public EpisodeDto toEpisodeDto(final Episode episode) {
        return new EpisodeDto(
                episode.getId(),
                episode.getName(),
                episode.getContent().getId(),
                episode.getSeasonNumber(),
                episode.getEpisodeNumber(),
                episode.getDurationMin(),
                episode.getDescription(),
                episode.getTrailerUrl(),
                episode.getVideoUrl(),
                DateTimeUtil.formatDate(episode.getReleaseDate())
        );
    }
}
