package com.app.MyIBC.StatistiqueEtExport.service;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final InscriptionRepository inscriptionRepository;
    private final DirigeantStatSercvice dirigeantStatSercvice;
    private final DirigeantRepository dirigeantRepository;

    public Long getTotalAmount(){
        return inscriptionRepository.findAll().stream().mapToLong(c -> c.getCamp().getPrix()).sum();
    }


    public Long getTotalAmountForTransportAll(){
        List<Dirigeant> dirigeants = dirigeantRepository.findAll();
        return dirigeants.stream().mapToLong(dirigeantStatSercvice::getTotalAmountForTransport).sum();
    }


}

