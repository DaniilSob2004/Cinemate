package com.example.cinemate.mapper.content;

import com.example.cinemate.dto.content.ContentDto;
import com.example.cinemate.service.business_db.contentactorservice.ContentActorService;
import com.example.cinemate.service.business_db.contentgenreservice.ContentGenreService;
import com.example.cinemate.service.business_db.contentwarningservice.ContentWarningService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ContentEnrichMapper {

    private final ContentGenreService contentGenreService;
    private final ContentActorService contentActorService;
    private final ContentWarningService contentWarningService;

    public ContentEnrichMapper(ContentGenreService contentGenreService, ContentActorService contentActorService, ContentWarningService contentWarningService) {
        this.contentGenreService = contentGenreService;
        this.contentActorService = contentActorService;
        this.contentWarningService = contentWarningService;
    }

    public void enrichContentDto(final List<ContentDto> contentsDto) {
        List<Integer> contentIds = contentsDto.stream().map(ContentDto::getId).toList();

        // 3 запроса и получаем все genres, actors и warnings для всех контентов
        Map<Integer, List<Integer>> genresMap = contentGenreService.getGenresByContentIds(contentIds);
        Map<Integer, List<Integer>> actorsMap = contentActorService.getActorsByContentIds(contentIds);
        Map<Integer, List<Integer>> warningsMap = contentWarningService.getWarningsByContentIds(contentIds);

        contentsDto.forEach(dto -> {
            dto.setGenres(genresMap.getOrDefault(dto.getId(), List.of()));
            dto.setActors(actorsMap.getOrDefault(dto.getId(), List.of()));
            dto.setWarnings(warningsMap.getOrDefault(dto.getId(), List.of()));
        });
    }
}
