package com.app.MyIBC.GestionDesUtilisateur.entity;


import com.app.MyIBC.Authentification.entity.User;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Utilisateur extends User {
    private Long CampFondationAmount = 0L;
    private Long CampJeuneAmount = 0L;
    private Long CampLeaderAmount = 0L;
}
