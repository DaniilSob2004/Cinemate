package com.example.cinemate.dto.genre.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenreFilesDto {
    private MultipartFile image;
}
