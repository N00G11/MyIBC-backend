package com.app.MyIBC.GestionInscription.entity;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private LocalDate date = LocalDate.now();
    private String nomComplet;
    private String sexe;
    private String telephone;
    private LocalDate dateNaissance;
    private String pays;
    private String ville;
    private String delegation;
    private String code;
    private Boolean badge = false;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
}
