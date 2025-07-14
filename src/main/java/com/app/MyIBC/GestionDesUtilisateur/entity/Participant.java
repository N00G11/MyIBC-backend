package com.app.MyIBC.GestionDesUtilisateur.entity;


import com.app.MyIBC.Authentification.entity.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Participant extends User {
    private LocalDate dateNaissance;
    private String sexe;
    private String telephone;
    private String pays;
    private String ville;
    private String delegation;
    private Boolean payTransport;
    private Boolean qrCode;
}
