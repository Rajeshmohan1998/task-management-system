package com.taskapp.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor               // JPA requires a no-arg constructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable =false)
    private String userName;

    @Column(nullable= false)
    private String password;

    @Column(unique=true, nullable= false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;

}
