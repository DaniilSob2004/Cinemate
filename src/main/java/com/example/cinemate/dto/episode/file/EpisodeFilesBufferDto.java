package com.example.cinemate.dto.episode.file;

import com.example.cinemate.dto.common.TempContentFile;
import lombok.*;

// для хранения файлов в байтах
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeFilesBufferDto {
    private TempContentFile trailer;
    private TempContentFile video;
}
