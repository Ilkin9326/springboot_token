package com.bhos.ticketbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequestDto {
    @NotBlank(message = "Invalid title: role title field can not be empty")
    @Size(min = 3, max = 70, message = "Invalid title: Must be of 3 - 70 characters")
    private String title;

    private String description;

}
