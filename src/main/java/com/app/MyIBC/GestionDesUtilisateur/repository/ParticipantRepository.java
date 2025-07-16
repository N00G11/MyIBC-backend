package com.app.MyIBC.GestionDesUtilisateur.repository;


import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByVille(String ville);
    List<Participant> findByDelegation(String delegation);
    Participant findByEmail(String email);

    boolean existsByusername(String nom);

    boolean existsByEmail(String email);
}
