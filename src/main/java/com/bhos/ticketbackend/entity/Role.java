package com.bhos.ticketbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer role_id;

    @Column(nullable = false, length = 50, unique = true)
    private String title_az;

    @Column(nullable = false, length = 50, unique = true)
    private String title_en;

    @Column(nullable = false, length = 50, unique = true)
    private String title_ru;

    public Role() { }

    public Role(String name) {
        this.title_az = name;
    }

    public Role(Integer id) {
        this.role_id = id;
    }


    @Override
    public String toString() {
        return this.title_en;
    }
}
