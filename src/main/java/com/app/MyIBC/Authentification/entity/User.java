package com.app.MyIBC.Authentification.entity;

import com.app.MyIBC.Authentification.utils.Role;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@JsonPropertyOrder({ "id", "username", "password", "email", "role" })
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String username;
    private String password;
    private String email;

    @Column(unique = true)
    private String code;
    private Role role = Role.ROLE_UTILISATEUR;
}
