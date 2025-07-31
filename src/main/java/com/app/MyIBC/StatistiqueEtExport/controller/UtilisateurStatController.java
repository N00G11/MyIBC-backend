package com.app.MyIBC.StatistiqueEtExport.controller;

import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.service.UserService;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.repository.UtilisateurRepository;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/statistique/utilisateur")
@RequiredArgsConstructor
public class UtilisateurStatController {

    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurService utilisateurService;
    private final CampRepository campRepository;
    private final UserService userService;

    // Nombre de participants d'un utilisateur à un camp
    @GetMapping("/numberParticipantsByCamp/{code}/{campId}")
    public ResponseEntity<Long> numberParticipantsByCamp(@PathVariable String code, @PathVariable Long campId) {
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camp non trouvé"));

        List<Inscription> inscriptions = userService.getAllInscriptionByUserCode(code);
        long count = inscriptions.stream()
                .filter(i -> i.getCamp().getId().equals(camp.getId()))
                .count();

        return ResponseEntity.ok(count);
    }

    // Somme totale payée par un utilisateur pour un camp
    @GetMapping("/totalAmountByCamp/{code}/{campId}")
    public ResponseEntity<Long> totalAmountByCamp(@PathVariable String code, @PathVariable Long campId) {
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camp non trouvé"));

        List<Inscription> inscriptions = userService.getAllInscriptionByUserCode(code);
        long total = inscriptions.stream()
                .filter(i -> i.getCamp().getId().equals(camp.getId()))
                .mapToLong(i -> i.getCamp().getPrix())
                .sum();

        return ResponseEntity.ok(total);
    }

    // Tous les participants liés à un utilisateur
    @GetMapping("/allParticipants/{code}")
    public ResponseEntity<List<Inscription>> allParticipants(@PathVariable String code) {
        List<Inscription> inscriptions = userService.getAllInscriptionByUserCode(code);
        return ResponseEntity.ok(inscriptions);
    }

    // Récupérer un utilisateur par son code
    @GetMapping("/code/{code}")
    public ResponseEntity<?> getUtilisateurByCode(@PathVariable String code) {
        User utilisateur = userService.getUserByCode(code);
        if (utilisateur == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé");
        }
        return ResponseEntity.ok(utilisateur);
    }
}
