package com.example.cinemate.dto.user.file;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilesDto {
    private MultipartFile avatar;
}
