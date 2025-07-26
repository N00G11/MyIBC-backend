package com.app.MyIBC.GestionPayement.repository;


import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionPayement.entity.Payement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PayementRepository extends JpaRepository<Payement, Long> {
    List<Inscription> findByUtilisateur(Utilisateur utilisateur);
}
