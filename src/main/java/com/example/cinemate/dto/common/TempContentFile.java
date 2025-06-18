package com.example.cinemate.dto.common;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TempContentFile {
    private byte[] content;
    private String originalFilename;
    private String contentType;
}
