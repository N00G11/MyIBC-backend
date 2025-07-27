package com.app.MyIBC.GestionDesUtilisateur.service;


import com.app.MyIBC.Authentification.utils.Role;
import com.app.MyIBC.GestionDesUtilisateur.entity.Tresorier;
import com.app.MyIBC.GestionDesUtilisateur.entity.Utilisateur;
import com.app.MyIBC.GestionDesUtilisateur.repository.TresorierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TresorierService {

    private final TresorierRepository tresorierRepository;

    public Tresorier saveTresorier(Tresorier tresorier){
        Tresorier t = new Tresorier();
        t.setUsername(tresorier.getUsername());
        t.setPays(tresorier.getPays());
        t.setTelephone(tresorier.getTelephone());
        t.setPassword(tresorier.getPassword());
        t.setRole(Role.valueOf("ROLE_TRESORIER"));

        // Étape 1 : Sauvegarde initiale pour obtenir l'ID
        t = tresorierRepository.save(t);

        // Étape 2 : Génération du code
        String usernamePart = tresorier.getUsername().length() >= 3
                ? tresorier.getUsername().substring(0, 3).toUpperCase()
                : String.format("%-3s", tresorier.getUsername()).replace(' ', 'X').toUpperCase(); // Cas username < 3 lettres

        String idPart = String.format("%05d", t.getId()); // id sur 5 chiffres
        String code = usernamePart + idPart;

        // Mise à jour du champ code
        t.setCode(code);

        // Étape 3 : Sauvegarde finale avec le code mis à jour
        return tresorierRepository.save(t);
    }
}
