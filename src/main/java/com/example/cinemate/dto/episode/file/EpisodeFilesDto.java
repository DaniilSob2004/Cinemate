package com.example.cinemate.dto.episode.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeFilesDto {
    private MultipartFile trailer;
    private MultipartFile video;
}
