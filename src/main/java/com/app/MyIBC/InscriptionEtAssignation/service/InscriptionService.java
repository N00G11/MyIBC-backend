package com.app.MyIBC.InscriptionEtAssignation.service;

import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import com.app.MyIBC.InscriptionEtAssignation.entity.Inscription;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InscriptionService {

    private final InscriptionRepository inscriptionRepository;
    private final ParticipantRepository participantRepository;
    private final CampRepository campRepository;
    private final DirigeantRepository dirigeantRepository;


    @Transactional
    public Inscription assignerDirigeantEquitablement(Participant participant, String email, Long campId) throws IOException, WriterException {
        Inscription inscription = new Inscription();
        inscription.setDate(LocalDate.now());
        String delegation = participant.getDelegation(); // Supposons que Participant a une propriété 'ville'

        // Récupérer tous les dirigeants de la même ville
        List<Dirigeant> dirigeantsVille = dirigeantRepository.findByDelegation(delegation);

        if (dirigeantsVille.isEmpty()) {
            throw new IllegalStateException("Aucun dirigeant disponible pour la delegation : " + delegation);
        }

        // Compter le nombre d'inscriptions par dirigeant pour la ville
        Map<Dirigeant, Long> compteurDirigeants = dirigeantsVille.stream()
                .collect(Collectors.toMap(
                        dirigeant -> dirigeant,
                        dirigeant -> inscriptionRepository.countByDirigeantAssigneAndVille(dirigeant, delegation)
                ));

        // Trouver la valeur minimale d'assignations
        long minAssignments = compteurDirigeants.values().stream()
                .min(Long::compare)
                .orElseThrow(() -> new IllegalStateException("Erreur lors de l'évaluation des dirigeants"));

        // Filtrer les dirigeants ayant ce minimum
        List<Dirigeant> candidats = compteurDirigeants.entrySet().stream()
                .filter(entry -> entry.getValue() == minAssignments)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Sélectionner aléatoirement un dirigeant parmi ceux ayant le moins d’assignations
        Dirigeant dirigeantAssigner = candidats.get(new Random().nextInt(candidats.size()));

        //assignation des Participant et des Camps
        Optional<Camp> camp = campRepository.findById(campId).map(c -> {
            c.setParticipants(c.getParticipants() + 1);
            return campRepository.save(c);
        });

        //mise a jour du participant
        Participant p = participantRepository.findByEmail(email);
        p.setVille(participant.getVille());
        p.setDelegation(participant.getDelegation());
        p.setPays(participant.getPays());
        p.setTelephone(participant.getTelephone());
        p.setSexe(participant.getSexe());
        p.setDateNaissance(participant.getDateNaissance());
        p.setQrCode(true);
        p.setPayTransport(participant.getPayTransport());

        participantRepository.save(p);





        inscription.setParticipant(p);
        inscription.setCamp(camp.get());

        // Assigner le dirigeant à l'inscription et mettre à jour le nombre d'assignations
        inscription.setDirigeantAssigne(dirigeantAssigner);
        dirigeantAssigner.setParticipants(dirigeantAssigner.getParticipants() + 1);
        return inscriptionRepository.save(inscription);
    }

}
