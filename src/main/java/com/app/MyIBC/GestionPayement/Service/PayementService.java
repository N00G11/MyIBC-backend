package com.app.MyIBC.GestionPayement.Service;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.service.UserService;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.repository.UtilisateurRepository;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import com.app.MyIBC.GestionPayement.entity.Payement;
import com.app.MyIBC.GestionPayement.repository.PayementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PayementService {

    private final PayementRepository payementRepository;
    private final InscriptionRepository inscriptionRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CampRepository campRepository;


    public Payement savePayement(String campType, String code, String codeTresorier) {
        Camp camp = campRepository.findCampByType(campType)
                .orElseThrow(() -> new RuntimeException("Camp de type \"" + campType + "\" introuvable"));

        User utilisateur = userService.getUserByCode(code);
        List<Inscription> inscriptions = userService.getInscriptionsByUserAndCamp(code, camp.getId());

        // Marquer les inscriptions comme payées
        inscriptions.forEach(i -> i.setBadge(true));
        inscriptionRepository.saveAll(inscriptions); // Plus efficace que plusieurs save()

        // Récupérer l'ancien montant avant mise à zéro
        Long montantPaye;
        switch (camp.getType()) {
            case "Camp des Agneaux" -> {
                montantPaye = utilisateur.getCampAgneauxAmount();
                camp.setParticipants(camp.getParticipants() + (montantPaye/ camp.getPrix()));
                utilisateur.setCampAgneauxAmount(0L);
            }
            case "Camp de la Fondation" -> {
                montantPaye = utilisateur.getCampFondationAmount();
                camp.setParticipants(camp.getParticipants() + (montantPaye/ camp.getPrix()));
                utilisateur.setCampFondationAmount(0L);
            }
            case "Camp des Leaders" -> {
                montantPaye = utilisateur.getCampLeaderAmount();
                camp.setParticipants(camp.getParticipants() + (montantPaye/ camp.getPrix()));
                utilisateur.setCampLeaderAmount(0L);
            }
            default -> throw new IllegalArgumentException("Type de camp inconnu : " + camp.getType());
        }

        userRepository.save(utilisateur);

        // Création et enregistrement du paiement
        Payement payement = new Payement();
        payement.setCodeTresorier(codeTresorier);
        payement.setCamp(camp);
        payement.setUtilisateur(utilisateur);
        payement.setMontant(montantPaye); // Enregistrement de l'ancien montant

        return payementRepository.save(payement);
    }

}
