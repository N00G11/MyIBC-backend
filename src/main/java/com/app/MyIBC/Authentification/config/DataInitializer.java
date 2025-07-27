package com.app.MyIBC.Authentification.config;

import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Admin;
import com.app.MyIBC.GestionDesUtilisateur.entity.Tresorier;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.TresorierRepository;
import com.app.MyIBC.GestionDesUtilisateur.service.TresorierService;
import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
import com.app.MyIBC.gestionDesLocalisation.repository.DelegationRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.PaysRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final AdminRepository adminRepository;
    private final TresorierService tresorierService;
    private final CampRepository campRepository;
    private final PaysRepository paysRepository;
    private final VilleRepository villeRepository;
    private final DelegationRepository delegationRepository;



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
            admin.setCode("00000");
            admin.setRole(Role.ROLE_ADMIN);
            adminRepository.save(admin);
            System.out.println("✔️ Admin ajouté avec succès.");
        } else {
            System.out.println("⚠️ Admin non ajouté : le username ou l’email est déjà utilisé.");
        }


        username = "tresorier";
        email = "trsorier@gmail.com";
        if (validate(username, email)) {
            Tresorier tresorier = new Tresorier();
            tresorier.setUsername(username);
            tresorier.setPassword(passwordEncoder.encode("1234"));
            tresorier.setEmail(email);
            tresorier.setRole(Role.ROLE_TRESORIER);
            tresorier = tresorierService.saveTresorier(tresorier);
            System.out.println("✔️ Tresorier ajouté avec succès.  code: " + tresorier.getCode());
        } else {
            System.out.println("⚠️ Admin non ajouté : le username ou l’email est déjà utilisé.");
        }


        String pays = "Cameroun";
        String ville = "bangangte";
        String delegation = "EEC";

        Pays p = new Pays();
        p.setName(pays);
        p = paysRepository.save(p);
        Ville v = new Ville();
        v.setName(ville);
        v.setPays(p);
        v = villeRepository.save(v);
        Delegation d = new Delegation();
        d.setName(delegation);
        d.setVille(v);
        delegationRepository.save(d);
        System.out.println("Localisation" + p.getName() + " " + v.getName() + "  " + d.getName() + "cree avec succes");





    }

    public boolean validate(String username, String email) {
        boolean usernameTaken =
                adminRepository.existsByusername(username);

        boolean emailTaken =
                adminRepository.existsByEmail(email);

        return !usernameTaken && !emailTaken;
    }
}
