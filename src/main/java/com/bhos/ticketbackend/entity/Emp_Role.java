package com.bhos.ticketbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "emp_roles")
public class Emp_Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ur_id;

    private int role_id;
    private int emp_id;


}
