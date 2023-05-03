package com.bhos.ticketbackend.auth;

import com.bhos.ticketbackend.dto.UserDTO;
import com.bhos.ticketbackend.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private UserDTO user;
    private String token;
}