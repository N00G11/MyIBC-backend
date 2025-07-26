package com.app.MyIBC.GestionInscription.service;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InscriptionService {
    private final InscriptionRepository inscriptionRepository;
    private final CampRepository campRepository;
    private final UtilisateurService utilisateurService;

    public Inscription saveInscription(Inscription inscription, String codeUtilisateur, Long campId) {
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new IllegalArgumentException("Camp not found with ID: " + campId));

        Utilisateur utilisateur = utilisateurService.getUserByCode(codeUtilisateur);
        if (utilisateur == null) {
            throw new IllegalArgumentException("Utilisateur not found with code: " + codeUtilisateur);
        }

        inscription.setCamp(camp);
        inscription.setUtilisateur(utilisateur);

        String campType = camp.getType();
        Long prix = camp.getPrix();

        switch (campType) {
            case "Camp des Agneaux" -> utilisateur.setCampAgneauxAmount(utilisateur.getCampAgneauxAmount() + prix);
            case "Camp des Jeunes"  -> utilisateur.setCampJeuneAmount(utilisateur.getCampJeuneAmount() + prix);
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
