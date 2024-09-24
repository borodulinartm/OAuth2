package org.example.auth_test.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterUserDTO {
    @NotBlank
    @NotNull
    private String userName;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotNull
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;
}
