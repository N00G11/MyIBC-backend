package com.app.MyIBC.GestionInscription.entity;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
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


    @ManyToOne
    @JoinColumn(name = "pays_id")
    private Pays pays;

    @ManyToOne
    @JoinColumn(name = "ville_id")
    private Ville ville;

    @ManyToOne
    @JoinColumn(name = "delegation_id")
    private Delegation delegation;


    private String code;
    private Boolean badge = false;


    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;
}
