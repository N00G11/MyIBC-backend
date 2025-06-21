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

    private final AdminRepository adminRepository;
    private final CampRepository campRepository;
    private final DirigeantRepository dirigeantRepository;
    private final ParticipantRepository participantRepository;
    private final PasswordEncoder passwordEncoder;

    private final Map<String, Map<String, List<String>>> countriesData = Map.of(
            "Cameroun", Map.of(
                    "Yaoundé", List.of("Délégation du Centre", "Délégation Nkolbisson", "Délégation Mvog-Ada"),
                    "Douala", List.of("Délégation Bonaberi", "Délégation Akwa", "Délégation Deïdo"),
                    "Bafoussam", List.of("Délégation Bapi", "Délégation Tamdja", "Délégation Kamkop"),
                    "Garoua", List.of("Délégation Plateau", "Délégation Poumpoumré", "Délégation Ngaoundéré"),
                    "Maroua", List.of("Délégation Domayo", "Délégation Kongola", "Délégation Hardé")
            ),
            "France", Map.of(
                    "Paris", List.of("Délégation Nord", "Délégation Sud", "Délégation Centre"),
                    "Lyon", List.of("Croix-Rousse", "Part-Dieu", "Gerland"),
                    "Marseille", List.of("Castellane", "Noailles", "La Joliette"),
                    "Toulouse", List.of("Compans", "Mirail", "Saint-Cyprien")
            )
    );

    @Override
    public void run(String... args) {
        initCamps();

        for (Map.Entry<String, Map<String, List<String>>> countryEntry : countriesData.entrySet()) {
            String country = countryEntry.getKey();
            Map<String, List<String>> villes = countryEntry.getValue();

            int cityIndex = 1;
            for (Map.Entry<String, List<String>> cityEntry : villes.entrySet()) {
                String ville = cityEntry.getKey();
                List<String> delegations = cityEntry.getValue();

                // Créer un Dirigeant pour chaque délégation de la ville
                for (String delegation : delegations) {
                    String username = ville.toLowerCase() + "_" + cityIndex;
                    String email = username + "@mail.com";

                    if (validate(username, email)) {
                        Dirigeant dirigeant = new Dirigeant();
                        dirigeant.setUsername(username);
                        dirigeant.setPassword(passwordEncoder.encode("1234"));
                        dirigeant.setEmail(email);
                        dirigeant.setRole(Role.ROLE_DIRIGEANT);
                        dirigeant.setPays(country);
                        dirigeant.setVille(ville);
                        dirigeant.setDelegation(delegation);
                        dirigeant.setTelephone("+0000000000");
                        dirigeantRepository.save(dirigeant);
                        System.out.println("✔️ Dirigeant '" + username + "' ajouté pour " + ville + " - " + delegation);
                    }
                    cityIndex++;
                }
            }
            // Un admin par ville (optionnel : tu peux aussi le faire par pays uniquement)
            String adminUsername = "admin";
            String adminEmail = adminUsername + "@mail.com";
            if (validate(adminUsername, adminEmail)) {
                Admin admin = new Admin();
                admin.setUsername(adminUsername);
                admin.setPassword(passwordEncoder.encode("1234"));
                admin.setEmail(adminEmail);
                admin.setRole(Role.ROLE_ADMIN);
                adminRepository.save(admin);
                System.out.println("✔️ Admin '" + adminUsername + "' ajouté pour la ville de ");
            }
        }
    }

    private void initCamps() {
        addCamp("Camp des Agneaux", "6-12", 10000L);
        addCamp("Camp des Jeunes", "13-20", 15000L);
    }

    private void addCamp(String type, String trancheAge, Long prix) {
        if (campRepository.findCampByType(type).isEmpty()) {
            Camp c1 = new Camp();
            c1.setType(type);
            c1.setDescription(type);
            c1.setTrancheAge(trancheAge);
            c1.setPrix(prix);
            campRepository.save(c1);
            System.out.println("✔️ " + type + " ajouté avec succès.");
        }
    }

    public boolean validate(String username, String email) {
        boolean usernameTaken = adminRepository.existsByusername(username)
                || dirigeantRepository.existsByusername(username)
                || participantRepository.existsByusername(username);
        boolean emailTaken = adminRepository.existsByEmail(email)
                || dirigeantRepository.existsByEmail(email)
                || participantRepository.existsByEmail(email);
        return !usernameTaken && !emailTaken;
    }
}
