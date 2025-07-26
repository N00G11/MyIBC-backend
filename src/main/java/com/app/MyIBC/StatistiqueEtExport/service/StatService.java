package com.app.MyIBC.StatistiqueEtExport.service;


import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatService {
    private final InscriptionRepository inscriptionRepository;

    public Long getTotalAmount(){
        return inscriptionRepository.findAll().stream().mapToLong(c -> c.getCamp().getPrix()).sum();
    }
}

