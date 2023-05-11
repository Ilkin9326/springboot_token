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
    @NotBlank(message = "Invalid title_az: title_az field can not be empty")
    @Size(min = 3, max = 70, message = "Invalid title_az: Must be of 3 - 70 characters")
    private String title_az;

    private String title_en;


    private String title_ru;
}
