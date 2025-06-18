package com.example.cinemate.dto.content.file;

import com.example.cinemate.dto.common.TempContentFile;
import lombok.*;

// для хранения файлов в байтах
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentFilesBufferDto {
    private TempContentFile poster;
    private TempContentFile trailer;
    private TempContentFile video;
}
