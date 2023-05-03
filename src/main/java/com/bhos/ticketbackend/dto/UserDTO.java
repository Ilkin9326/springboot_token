package com.bhos.ticketbackend.dto;

import com.bhos.ticketbackend.user.Role;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;

    private Role role;

}
