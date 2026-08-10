package com.taskapp.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    private String userName;

    @NotBlank
    @Size(min = 8,message = "Password is mandatory")
    private String password;

    @NotBlank
    @Email
    private String email;
}
