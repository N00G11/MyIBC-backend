package com.app.MyIBC.StatistiqueEtExport.service;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatService {

    private final InscriptionRepository inscriptionRepository;
    private final CampRepository campRepository;

    public Long getTotalAmount(){
        return inscriptionRepository.findAll().stream().mapToLong(c -> c.getCamp().getPrix()).sum();
    }


}

