package com.app.MyIBC.InscriptionEtAssignation.entity;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private LocalDate date;
    private String nom;
    private String prenom;
    private String sexe;
    private String telephone;
    private LocalDate dateNaissance;
    private String pays;
    private String ville;
    private String delegation;

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "dirigeant_id")
    private Dirigeant dirigeantAssigne;
}
