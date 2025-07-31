package com.app.MyIBC.Authentification.service;


import com.app.MyIBC.Authentification.entity.User;
import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final InscriptionRepository inscriptionRepository;
    private final CampRepository campRepository;

    public User getUserByCode(String code){
        List<User> users = userRepository.findAll();

        for(User u:users){
            if (code.equals(u.getCode())){
                return u;
            }
        }
        return null;
    }

    public User getUserByTelephone(String telephone){
        List<User> users = userRepository.findAll();

        for(User u:users){
            if (telephone.equals(u.getTelephone())){
                return u;
            }
        }
        return null;
    }

    public List<Inscription> getAllInscriptionByUserCode(String code) {
        User utilisateurOpt = getUserByCode(code);

        if (utilisateurOpt == null) {
            return Collections.emptyList(); // ou tu peux lancer une exception personnalisée
        }

        return inscriptionRepository.findByUtilisateur(utilisateurOpt);
    }


    public List<Inscription> getInscriptionsByUserAndCamp(String userCode, Long campId) {
        User utilisateur = getUserByCode(userCode);
        Camp camp = campRepository.findById(campId)
                .orElseThrow(() -> new IllegalArgumentException("Camp non trouvé"));

        return inscriptionRepository.findByUtilisateurAndCamp(utilisateur, camp);
    }
}
