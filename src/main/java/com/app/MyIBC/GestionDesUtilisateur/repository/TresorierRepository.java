package com.app.MyIBC.GestionDesUtilisateur.repository;


import com.app.MyIBC.GestionDesUtilisateur.entity.Tresorier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TresorierRepository extends JpaRepository<Tresorier, Long> {
}
