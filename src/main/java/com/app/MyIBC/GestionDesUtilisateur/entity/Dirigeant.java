package com.app.MyIBC.GestionDesUtilisateur.entity;


import com.app.MyIBC.Authentification.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Dirigeant extends User {
    private String telephone;
    private String pays;
    private String ville;
    private String delegation;
    private Long participants = 0L;
}
