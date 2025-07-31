package com.app.MyIBC.GestionPayement.entity;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Payement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long montant = 0L;
    private String CodeTresorier;

    private LocalDate date = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "camp_id")
    private Camp camp;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User utilisateur;

}
