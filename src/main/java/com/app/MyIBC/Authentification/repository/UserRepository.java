package com.app.MyIBC.Authentification.repository;

import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByTelephone(String telephone);
    User findByEmail(String email);
}
