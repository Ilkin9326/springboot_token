package com.bhos.ticketbackend.auth;

import com.bhos.ticketbackend.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private EmployeeDTO user;
    private String token;
}