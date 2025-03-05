package com.example.cinemate.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateAdminDto extends UserUpdateDto {
    /*private String username;
    private String firstname;
    private String surname;

    @NotBlank(message = "Email should not be blank")
    private String email;

    private String phoneNum;*/
    private String password;
    //private String avatar;

    @Size(min = 1, message = "The roles list must contain at least one role")
    private List<String> roles;

    @JsonProperty("isActive")
    private boolean isActive;
}
