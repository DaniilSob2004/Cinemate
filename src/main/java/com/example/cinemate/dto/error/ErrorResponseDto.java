package com.example.cinemate.dto.error;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {
    private String message;
    private int status;
}
