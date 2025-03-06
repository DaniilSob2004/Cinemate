package com.example.cinemate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAdminDto extends UserUpdateDto {
    private String password;

    @Size(min = 1, message = "The roles list must contain at least one role")
    private List<String> roles;

    @JsonProperty("isActive")
    private boolean isActive;
}
