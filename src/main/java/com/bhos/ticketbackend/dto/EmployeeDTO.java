package com.bhos.ticketbackend.dto;

import com.bhos.ticketbackend.entity.Role;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Integer emp_id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;

    private Set<Role> roles = new HashSet<>();

}
