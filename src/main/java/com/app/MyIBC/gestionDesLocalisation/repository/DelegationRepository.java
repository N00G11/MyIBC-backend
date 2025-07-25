package com.app.MyIBC.gestionDesLocalisation.repository;


import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation,Long> {
}
