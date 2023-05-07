package com.bhos.ticketbackend.controllers;

import com.bhos.ticketbackend.auth.AuthenticationService;
import com.bhos.ticketbackend.dto.ResponseDTO;
import com.bhos.ticketbackend.dto.EmployeeDTO;
import com.bhos.ticketbackend.user.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class DemoController {

    private final AuthenticationService service;
    private final JdbcTemplate jdbcTemplate;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<String> sayHello(Principal principal) {
        return ResponseEntity.ok("Hello from secured endpoint " + principal.getName());
    }

    @GetMapping("/users")
    public ResponseEntity<ResponseDTO> getUsers() {
        List<EmployeeDTO> listDTo = null;
        try {
            String sql = "select u.id, u.firstname, u.lastname, u.email, u.role from user u";
            listDTo = jdbcTemplate.query(sql, new UserRowMapper());
        }catch (EmptyResultDataAccessException exception){
            exception.printStackTrace();
            logger.info(exception.getMessage());
        }

        return ResponseEntity.ok(ResponseDTO.of(listDTo, "Success response"));
    }

}