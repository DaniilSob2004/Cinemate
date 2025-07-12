package com.example.cinemate.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used for get oauth tokens")
public class OauthStateDto {

    @Schema(example = "aaa-111-bbb-2222")
    @NotBlank(message = "State id should not be blank")
    private String state;
}
