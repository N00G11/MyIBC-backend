package com.app.MyIBC.GestionDesUtilisateur.Controller;


import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dirigeant")
@RequiredArgsConstructor
public class DirigeantController {

    private final DirigeantRepository dirigeantRepository;
    private final ParticipantRepository participantRepository;
    private final AdminRepository adminRepository;
    private  final PasswordEncoder passwordEncoder;


    @GetMapping("/all")
    public List<Dirigeant> getAllDirigeant(){
        return dirigeantRepository.findAll();
    }

    @GetMapping("/pays")
    public List<Dirigeant> getDirigeantByPays(String pays){
        return dirigeantRepository.findByPays(pays);
    }

    @GetMapping("/ville")
    public List<Dirigeant> getDirigeantByVille(String ville){
        return dirigeantRepository.findByVille(ville);
    }

    @GetMapping("/delegation")
    public List<Dirigeant> getDirigeantByDelegation(String delegation){
        return dirigeantRepository.findByDelegation(delegation);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDirigeant(@RequestBody Dirigeant dirigeant) {
        String email = dirigeant.getEmail();
        String nom = dirigeant.getUsername();

        // Vérification d'existence d'un utilisateur avec le même email ou nom
        boolean emailExists = participantRepository.existsByEmail(email)
                || dirigeantRepository.existsByEmail(email)
                || adminRepository.existsByEmail(email);

        boolean nomExists = participantRepository.existsByusername(nom)
                || dirigeantRepository.existsByusername(nom)
                || adminRepository.existsByusername(nom);

        if (emailExists) {
            return ResponseEntity
                    .badRequest()
                    .body("Un utilisateur avec cet email existe déjà.");
        }

        if (nomExists) {
            return ResponseEntity
                    .badRequest()
                    .body("Un utilisateur avec ce nom existe déjà.");
        }

        // Si aucune collision, on continue l’enregistrement
        dirigeant.setRole(Role.ROLE_DIRIGEANT);
        dirigeant.setPassword(passwordEncoder.encode(dirigeant.getPassword()));
        Dirigeant savedDirigeant = dirigeantRepository.save(dirigeant);
        return ResponseEntity.ok(savedDirigeant);
    }

    @PutMapping("/update/{id}")
    public Optional<Dirigeant> updateDirigeant(@PathVariable Long id, @RequestBody Dirigeant dirigeant){
        return dirigeantRepository.findById(id).map(d -> {
            d.setPays(dirigeant.getPays());
            d.setVille(dirigeant.getVille());
            d.setDelegation(dirigeant.getDelegation());
            return dirigeantRepository.save(d);
        });
    }

    @DeleteMapping("/delete/{id}")
    public void deleteDirigeant(@PathVariable Long id){
        dirigeantRepository.deleteById(id);
    }


}
