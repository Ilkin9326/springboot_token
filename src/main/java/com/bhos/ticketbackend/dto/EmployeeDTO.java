package com.bhos.ticketbackend.dto;

import com.bhos.ticketbackend.user.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Integer emp_id;
    private String firstname;
    private String lastname;
    private String username;
    private String email;

    private Role role;

}
