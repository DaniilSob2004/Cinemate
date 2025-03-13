package com.example.cinemate.dto.user;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchParamsDto {
    @Min(value = 1, message = "Page number is not valid")
    private int page = 1;

    @Min(value = 1, message = "Size must be greater than or equal to 1")
    @Max(value = 50, message = "Size must be less than or equal to 50")
    private int size = 10;

    private String sortBy = "id";
    private Boolean isAsc = true;
    private String searchStr = "";
}
