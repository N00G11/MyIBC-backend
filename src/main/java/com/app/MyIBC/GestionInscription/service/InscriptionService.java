package com.app.MyIBC.GestionInscription.service;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.service.UserService;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
import com.app.MyIBC.gestionDesLocalisation.repository.DelegationRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.PaysRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InscriptionService {
    private final InscriptionRepository inscriptionRepository;
    private final CampRepository campRepository;
    private final UtilisateurService utilisateurService;
    private final UserService userService;
    private final PaysRepository paysRepository;
    private final VilleRepository villeRepository;
    private final DelegationRepository delegationRepository;

    public Inscription saveInscription(Inscription inscription, String codeUtilisateur, Long campId, Long idPays, Long idVille, Long idDelegation) {
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new IllegalArgumentException("Camp not found with ID: " + campId));

        Pays pays = paysRepository.findById(idPays)
                .orElseThrow(() -> new IllegalArgumentException("Pays not found with ID: " + idPays));

        Ville ville = villeRepository.findById(idVille)
                .orElseThrow(() -> new IllegalArgumentException("Ville not found with ID: " + idVille));

        Delegation delegation = delegationRepository.findById(idDelegation)
                .orElseThrow(() -> new IllegalArgumentException("Delegation not found with ID: " + idDelegation));

        User utilisateur = userService.getUserByCode(codeUtilisateur);
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur not found with code: " + codeUtilisateur);
        }

        inscription.setPays(pays);
        inscription.setVille(ville);
        inscription.setDelegation(delegation);
        inscription.setCamp(camp);
        inscription.setUtilisateur(utilisateur);

        String campType = camp.getType();
        Long prix = camp.getPrix();

        switch (campType) {
            case "Camp des Agneaux" -> utilisateur.setCampAgneauxAmount(utilisateur.getCampAgneauxAmount() + prix);
            case "Camp de la Fondation"  -> utilisateur.setCampFondationAmount(utilisateur.getCampFondationAmount() + prix);
            case "Camp des Leaders" -> utilisateur.setCampLeaderAmount(utilisateur.getCampLeaderAmount() + prix);
            default -> throw new IllegalArgumentException("Type de camp inconnu : " + campType);
        }

        // 1. On sauvegarde l'inscription sans le code (pour avoir l'ID auto-généré)
        Inscription saved = inscriptionRepository.save(inscription);

        // 2. Génération du code sur 5 caractères à partir de l'ID
        String codeInscription = String.format("%05d", saved.getId());
        saved.setCode(codeInscription);

        // 3. Sauvegarde finale avec le code
        return inscriptionRepository.save(saved);
    }


}
