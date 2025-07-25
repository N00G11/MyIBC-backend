package com.app.MyIBC.gestionDesLocalisation.repository;


import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Long> {
}
