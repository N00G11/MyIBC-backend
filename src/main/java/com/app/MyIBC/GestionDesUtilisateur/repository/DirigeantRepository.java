package com.app.MyIBC.GestionDesUtilisateur.repository;

import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirigeantRepository extends JpaRepository<Dirigeant, Long> {
    List<Dirigeant> findByVille(String ville);
    List<Dirigeant> findByDelegation(String delegation);
    List<Dirigeant> findByPays(String pays);

    boolean existsByEmail(String email);

    boolean existsByusername(String nom);

    Dirigeant findByEmail(String email);
}
