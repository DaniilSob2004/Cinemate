package com.example.cinemate.model.email;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailContent {
    private String subject;
    private String message;
}
