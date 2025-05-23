package com.example.cinemate.dto.contenttype;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentTypeDto {

    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;

    private String description;
    private String tags;
}
