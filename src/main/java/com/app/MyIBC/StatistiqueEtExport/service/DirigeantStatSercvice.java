package com.app.MyIBC.StatistiqueEtExport.service;


import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.InscriptionEtAssignation.entity.Inscription;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirigeantStatSercvice {

    private final DirigeantRepository dirigeantRepository;
    private final InscriptionRepository inscriptionRepository;


    public List<Inscription> getAllInscriptionByDirigeant(Dirigeant dirigeant){
        return inscriptionRepository.findByDirigeantAssigne(dirigeant);
    }

    public Long getTotalAmountForTransport(Dirigeant dirigeant){
        List<Inscription> inscriptions = getAllInscriptionByDirigeant(dirigeant);
        return inscriptions.stream().mapToLong(c -> c.getParticipant().getPayTransport() ? 10000L : 0L).sum();
    }


    public Long getTotalAmount(Dirigeant dirigeant){
        List<Inscription> inscriptions = getAllInscriptionByDirigeant(dirigeant);
        return inscriptions.stream().mapToLong(c -> c.getCamp().getPrix()).sum();
    }

    public Long getTotalAmountByCamp(Dirigeant dirigeant, Long campId){
        List<Inscription> inscriptions = getAllInscriptionByDirigeant(dirigeant);
        return inscriptions.stream().filter(c -> c.getCamp().getId().equals(campId)).mapToLong(c -> c.getCamp().getPrix()).sum();
    }

    public Long getNumberParticipantsByCamp(Dirigeant dirigeant, Long campId){
        return getAllInscriptionByDirigeant(dirigeant).stream().filter(c -> c.getCamp().getId().equals(campId)).count();
    }
}
