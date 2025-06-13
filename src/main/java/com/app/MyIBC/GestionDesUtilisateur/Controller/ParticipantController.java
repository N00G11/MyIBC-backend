package com.app.MyIBC.GestionDesUtilisateur.Controller;


import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.AdminRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/participant")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantRepository participantRepository;
    private final DirigeantRepository dirigeantRepository;
    private final AdminRepository adminRepository;

    @GetMapping("/all")
    public List<Participant> getAllParticipants() {
        return participantRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public Participant getParticipantByEmail(@PathVariable String email) {
        return participantRepository.findByEmail(email);
    }

    @GetMapping("/ville")
    public List<Participant> getParticipantsByVille(@RequestParam String ville) {
        return participantRepository.findByVille(ville);
    }

    @GetMapping("/delegation")
    public List<Participant> getParticipantsByDelegation(@RequestParam String delegation) {
        return participantRepository.findByDelegation(delegation);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
        String email = participant.getEmail();
        String nom = participant.getUsername();

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
        participant.setRole(Role.ROLE_DIRIGEANT);
        Participant savedDirigeant = participantRepository.save(participant);
        return ResponseEntity.ok(savedDirigeant);
    }

    @PutMapping("/update/{id}")
    public Optional<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participant) {
        return participantRepository.findById(id).map(p -> {
            p.setDateNaissance(participant.getDateNaissance());
            p.setSexe(participant.getSexe());
            p.setTelephone(participant.getTelephone());
            p.setVille(participant.getVille());
            p.setDelegation(participant.getDelegation());
            return participantRepository.save(p);
        });
    }

    @DeleteMapping("/delete/{id}")
    public void deleteParticipant(@PathVariable Long id) {
        participantRepository.deleteById(id);
    }
}
