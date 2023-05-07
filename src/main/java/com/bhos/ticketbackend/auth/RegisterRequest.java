package com.bhos.ticketbackend.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Invalid firstname: firstname field can not be empty")
    @Size(min = 3, max = 70, message = "Invalid firstname: Must be of 3 - 70 characters")
    private String firstname;

    @NotBlank(message = "Invalid lastname: lastname field can not be empty")
    @Size(min = 3, max = 70, message = "Invalid lastname: Must be of 3 - 70 characters")
    private String lastname;

    @NotBlank(message = "Invalid Name: name field can not be empty")
    @Size(min = 3, max = 70, message = "Invalid Name: Must be of 3 - 70 characters")
    private String username;

    @NotNull
    @Min(1)
    @Max(3)
    private int status;
    @NotNull(message = "Please enter a valid Email")
    @NotEmpty(message = "Email empty ola bilmez")
    @Email(message = "Email should be valid")
    private String email;


    private String password;
}