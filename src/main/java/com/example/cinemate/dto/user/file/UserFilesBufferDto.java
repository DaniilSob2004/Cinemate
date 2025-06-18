package com.example.cinemate.dto.user.file;

import com.example.cinemate.dto.common.TempContentFile;
import lombok.*;

// для хранения файлов в байтах
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFilesBufferDto {
    private TempContentFile avatar;
}
