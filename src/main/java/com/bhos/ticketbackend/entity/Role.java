package com.bhos.ticketbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer role_id;

    @Column(nullable = false, length = 50, unique = true)
    private String title;


    public Role() { }

    public Role(String title) {
        this.title = title;
    }

    public Role(Integer id) {
        this.role_id = id;
    }


    @Override
    public String toString() {
        return this.title;
    }
}
