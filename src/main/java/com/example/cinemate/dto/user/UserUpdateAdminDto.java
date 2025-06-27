package com.example.cinemate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "DTO containing updated user information")
public class UserUpdateAdminDto extends UserUpdateDto {

    @Schema(example = "12345")
    private String password;

    @Schema(example = "[\"ROLE_USER\"]")
    @Size(min = 1, max = 10, message = "The roles list must contain at least one and not more 10 roles")
    private List<String> roles;

    @Schema(example = "true")
    @JsonProperty("isActive")
    private boolean isActive;
}
