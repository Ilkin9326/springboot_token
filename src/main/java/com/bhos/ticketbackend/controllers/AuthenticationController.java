package com.bhos.ticketbackend.controllers;

import com.bhos.ticketbackend.dto.AuthenticationRequest;
import com.bhos.ticketbackend.auth.AuthenticationResponse;
import com.bhos.ticketbackend.auth.RegisterRequest;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.RoleRequestDto;
import com.bhos.ticketbackend.serviceImpl.EmployeeServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final EmployeeServiceImpl service;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.signUp(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.signIn(request));
    }


}
