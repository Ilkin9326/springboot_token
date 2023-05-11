package com.bhos.ticketbackend.service;

import com.bhos.ticketbackend.dto.AuthenticationRequest;
import com.bhos.ticketbackend.auth.AuthenticationResponse;
import com.bhos.ticketbackend.auth.RegisterRequest;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;

public interface IEmployeeService {
    ResponseDTO signUp(RegisterRequest request);
    AuthenticationResponse signIn(AuthenticationRequest request);

}
