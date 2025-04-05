package com.example.cinemate.dto.warning;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningDto {

    private Integer id;

    @NotBlank(message = "Name should not be blank")
    private String name;
}
