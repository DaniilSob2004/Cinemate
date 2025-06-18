package com.example.cinemate.dto.content.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentFilesDto {
    private MultipartFile poster;
    private MultipartFile trailer;
    private MultipartFile video;
}
