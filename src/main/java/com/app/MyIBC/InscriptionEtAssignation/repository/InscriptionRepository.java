package com.app.MyIBC.InscriptionEtAssignation.repository;


import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.InscriptionEtAssignation.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {
    
    @Query("SELECT COUNT(i) FROM Inscription i " +
           "JOIN i.participant p " +
           "WHERE i.dirigeantAssigne = :dirigeant AND p.ville = :ville")
    long countByDirigeantAssigneAndVille(
        @Param("dirigeant") Dirigeant dirigeant,
        @Param("ville") String ville
    );

    List<Inscription> findByDirigeantAssigne(Dirigeant dirigeant);

    Inscription findByParticipantEmail(String email);
}

