package com.example.cinemate.dto.genre.file;

import com.example.cinemate.dto.common.TempContentFile;
import lombok.*;

// для хранения файлов в байтах
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreFilesBufferDto {
    private TempContentFile image;
}
