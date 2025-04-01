package com.example.cinemate.dto.contenttype;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
