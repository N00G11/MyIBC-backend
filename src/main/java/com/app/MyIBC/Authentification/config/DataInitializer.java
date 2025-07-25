package com.app.MyIBC.Authentification.config;

import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Admin;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final DirigeantRepository dirigeantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final CampRepository campRepository;
    private final ParticipantRepository participantRepository;



    @Override
    public void run(String... args) {
        // ... Ton code d'initialisation Camp & Admin ici ...

        String type = "Camp des Agneaux";
        String description = "Camp des Agneaux";
        String trancheAge = "6-12";
        Long prix = 10000L;
        if (campRepository.findCampByType(type).isEmpty()){
            Camp c1 = new Camp();
            c1.setType(type);
            c1.setDescription(description);
            c1.setTrancheAge(trancheAge);
            c1.setPrix(prix);
            campRepository.save(c1);
            System.out.println("✔️ le Camp des Agneaux ajouté avec succès.");
        }


        type = "Camp des Jeunes";
        description = "Camp des Jeunes";
        trancheAge = "13-20";
        prix = 10000L;
        if (campRepository.findCampByType(type).isEmpty()){
            Camp c1 = new Camp();
            c1.setType(type);
            c1.setDescription(description);
            c1.setTrancheAge(trancheAge);
            c1.setPrix(prix);
            campRepository.save(c1);
            System.out.println("✔️ le Camp des Jeunes ajouté avec succès.");
        }

        type = "Camp des Leaders";
        description = "Camp des leaders";
        trancheAge = "21-50";
        prix = 15000L;
        if (campRepository.findCampByType(type).isEmpty()){
            Camp c1 = new Camp();
            c1.setType(type);
            c1.setDescription(description);
            c1.setTrancheAge(trancheAge);
            c1.setPrix(prix);
            campRepository.save(c1);
            System.out.println("✔️ le Camp des Jeunes ajouté avec succès.");
        }


        String username = "admin";
        String email = "admin@gmail.com";
        if (validate(username, email)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setEmail(email);
            admin.setRole(Role.ROLE_ADMIN);
            adminRepository.save(admin);
            System.out.println("✔️ Admin ajouté avec succès.");
        } else {
            System.out.println("⚠️ Admin non ajouté : le username ou l’email est déjà utilisé.");
        }

    }

    public boolean validate(String username, String email) {
        boolean usernameTaken =
                adminRepository.existsByusername(username) ||
                        dirigeantRepository.existsByusername(username) ||
                        participantRepository.existsByusername(username);

        boolean emailTaken =
                adminRepository.existsByEmail(email) ||
                        dirigeantRepository.existsByEmail(email) ||
                        participantRepository.existsByEmail(email);

        return !usernameTaken && !emailTaken;
    }
}
