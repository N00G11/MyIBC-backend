package com.app.MyIBC.GestionDesUtilisateur.service;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.repository.UtilisateurRepository;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    public final InscriptionRepository inscriptionRepository;
    private final UserRepository userRepository;
    private final CampRepository campRepository;

    public Utilisateur getUserByCode(String code) {
        return utilisateurRepository.findByCode(code).get();
    }


    public Utilisateur saveUtilisateur(User user){
        Utilisateur u = new Utilisateur();
        u.setUsername(user.getUsername());
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setRole(Role.valueOf("ROLE_UTILISATEUR"));

        // Étape 1 : Sauvegarde initiale pour obtenir l'ID
        u = utilisateurRepository.save(u);

        // Étape 2 : Génération du code
        String usernamePart = user.getUsername().length() >= 3
                ? user.getUsername().substring(0, 3).toUpperCase()
                : String.format("%-3s", user.getUsername()).replace(' ', 'X').toUpperCase(); // Cas username < 3 lettres

        String idPart = String.format("%05d", u.getId()); // id sur 5 chiffres
        String code = usernamePart + idPart;

        // Mise à jour du champ code
        u.setCode(code);

        // Étape 3 : Sauvegarde finale avec le code mis à jour
        return utilisateurRepository.save(u);
    }

    public List<Inscription> getAllInscriptionByUserCode(String code) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByCode(code);

        if (utilisateurOpt.isEmpty()) {
            return Collections.emptyList(); // ou tu peux lancer une exception personnalisée
        }

        return inscriptionRepository.findByUtilisateur(utilisateurOpt.get());
    }


    public List<Inscription> getInscriptionsByUserAndCamp(String userCode, Long campId) {
        Utilisateur utilisateur = getUserByCode(userCode);
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new IllegalArgumentException("Camp non trouvé"));

        return inscriptionRepository.findByUtilisateurAndCamp(utilisateur, camp);
    }

}
