package com.app.MyIBC.GestionInscription.repository;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    List<Inscription> findByUtilisateur(Utilisateur utilisateur);
    List<Inscription> findByUtilisateurAndCamp(Utilisateur utilisateur, Camp camp);
}

