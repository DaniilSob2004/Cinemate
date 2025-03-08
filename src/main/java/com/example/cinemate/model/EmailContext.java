package com.example.cinemate.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailContext {
    private String to;
    private String subject;
    private String message;
}
