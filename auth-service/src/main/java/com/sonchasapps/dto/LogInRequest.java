package com.sonchasapps.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }
}
