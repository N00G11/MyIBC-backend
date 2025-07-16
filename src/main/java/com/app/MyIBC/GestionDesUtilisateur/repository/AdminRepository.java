package com.app.MyIBC.GestionDesUtilisateur.repository;


import com.app.MyIBC.GestionDesUtilisateur.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Long> {
    boolean existsByEmail(String email);

    boolean existsByusername(String nom);
}
