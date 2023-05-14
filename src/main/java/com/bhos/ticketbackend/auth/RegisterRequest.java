package com.bhos.ticketbackend.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @NotNull(message = "Invalid status: status field can't be null")
    @Min(value = 1, message = "Invalid status: Must be of 1 - 3 number")
    @Max(value = 3, message = "Invalid status: Must be of 1 - 3 number")
    private int status;
    @NotNull(message = "Please enter a valid Email")
    @NotEmpty(message = "Email can't be empty")
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")
    private String email;


    private String password;

    @NotNull(message = "Please enter a valid Roles")
    @NotEmpty(message = "Role field can't be empty")
    private List<String> roles;
}