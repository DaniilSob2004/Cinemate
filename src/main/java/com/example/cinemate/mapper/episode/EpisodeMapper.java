package com.example.cinemate.mapper.episode;

import com.example.cinemate.dto.episode.EpisodeDto;
import com.example.cinemate.mapper.common.ModelProxyLinkMapper;
import com.example.cinemate.model.db.Episode;
import com.example.cinemate.service.amazon.AmazonS3Service;
import com.example.cinemate.utils.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EpisodeMapper {

    private final AmazonS3Service amazonS3Service;
    private final ModelProxyLinkMapper modelProxyLinkMapper;

    public EpisodeMapper(AmazonS3Service amazonS3Service, ModelProxyLinkMapper modelProxyLinkMapper) {
        this.amazonS3Service = amazonS3Service;
        this.modelProxyLinkMapper = modelProxyLinkMapper;
    }

    public EpisodeDto toEpisodeDto(final Episode episode) {
        return new EpisodeDto(
                episode.getId(),
                episode.getName(),
                episode.getContent().getId(),
                episode.getSeasonNumber(),
                episode.getEpisodeNumber(),
                episode.getDurationMin(),
                episode.getDescription(),
                amazonS3Service.getCloudFrontUrl(episode.getTrailerUrl()),
                amazonS3Service.getCloudFrontUrl(episode.getVideoUrl()),
                DateTimeUtil.formatDate(episode.getReleaseDate())
        );
    }

    public Episode toEpisode(final EpisodeDto episodeDto) {
        return new Episode(
                episodeDto.getId(),
                episodeDto.getName(),
                modelProxyLinkMapper.getContentById(episodeDto.getContentId()),
                episodeDto.getSeasonNumber(),
                episodeDto.getEpisodeNumber(),
                episodeDto.getDurationMin(),
                episodeDto.getDescription(),
                episodeDto.getTrailerUrl(),
                episodeDto.getVideoUrl(),
                LocalDate.parse(episodeDto.getReleaseDate()),
                null
        );
    }
}
