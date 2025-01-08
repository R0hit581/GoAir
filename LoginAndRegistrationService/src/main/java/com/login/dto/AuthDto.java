package com.login.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    /*
     * DTO (Data Transfer Object) for authentication requests.
     * This class is used to capture and validate user input for login operations.
     */

    @NotEmpty(message = "Please provide username and email...")
    // Ensures the email field is not empty and provides a custom error message.
    private String email;

    @NotEmpty(message = "Please give a password...")
    // Ensures the password field is not empty and provides a custom error message.
    private String password;
}
