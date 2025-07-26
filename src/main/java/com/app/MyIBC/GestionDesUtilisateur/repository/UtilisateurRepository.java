package com.app.MyIBC.GestionDesUtilisateur.repository;

import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Optional<Utilisateur> findByCode(String code);
}
