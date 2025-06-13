package com.app.MyIBC.Authentification.config;

import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesCamps.utils.TypeCamp;
import com.app.MyIBC.GestionDesUtilisateur.entity.Admin;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final CampRepository campRepository;
    private final DirigeantRepository dirigeantRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
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


        String username = "Gyls";
        String email = "gnsanan@gmail.com";
        if (validate(username, email)) {
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(passwordEncoder.encode("1234"));
            admin.setEmail(email);
            admin.setRole(Role.ROLE_ADMIN);
            adminRepository.save(admin);
            System.out.println("✔️ Admin 'Gyls' ajouté avec succès.");
        } else {
            System.out.println("⚠️ Admin non ajouté : le username ou l’email est déjà utilisé.");
        }

        username = "Nsanan";
        email = "nsanan@gmail.com";
        if (validate(username, email)) {
            Dirigeant dirigeant = new Dirigeant();
            dirigeant.setUsername(username);
            dirigeant.setPassword(passwordEncoder.encode("1234"));
            dirigeant.setEmail(email);
            dirigeant.setRole(Role.ROLE_DIRIGEANT);
            dirigeant.setPays("Cameroun");
            dirigeant.setDelegation("EEC");
            dirigeant.setVille("BANGANGTE");
            dirigeant.setTelephone("+237 690970730");
            dirigeantRepository.save(dirigeant);
            System.out.println("✔️ Dirigeant 'nsanan' ajouté avec succès.");
        } else {
            System.out.println("⚠️ Dirigeant non ajouté : le username ou l’email est déjà utilisé.");
        }

        username = "Ngongang";
        email = "ngongang@gmail.com";
        if (validate(username, email)) {
            Dirigeant dirigeant = new Dirigeant();
            dirigeant.setUsername(username);
            dirigeant.setPassword(passwordEncoder.encode("1234"));
            dirigeant.setEmail(email);
            dirigeant.setRole(Role.ROLE_DIRIGEANT);
            dirigeant.setPays("Cameroun");
            dirigeant.setDelegation("CMCI");
            dirigeant.setVille("YAOUNDE");
            dirigeant.setTelephone("+237 648520148");
            dirigeantRepository.save(dirigeant);
            System.out.println("✔️ Dirigeant 'ngongang' ajouté avec succès.");
        } else {
            System.out.println("⚠️ Dirigeant non ajouté : le username ou l’email est déjà utilisé.");
        }
    }

    public boolean validate(String username, String email){
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
