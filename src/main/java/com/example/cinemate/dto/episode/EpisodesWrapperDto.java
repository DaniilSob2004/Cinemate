package com.example.cinemate.dto.episode;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodesWrapperDto {
    private List<EpisodeDto> episodes;
}
