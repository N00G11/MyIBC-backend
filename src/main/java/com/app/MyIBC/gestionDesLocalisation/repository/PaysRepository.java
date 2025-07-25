package com.app.MyIBC.gestionDesLocalisation.repository;


import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaysRepository extends JpaRepository<Pays, Long> {
}
