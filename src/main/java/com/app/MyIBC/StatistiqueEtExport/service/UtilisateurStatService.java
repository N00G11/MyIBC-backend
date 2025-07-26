package com.app.MyIBC.StatistiqueEtExport.service;


import com.app.MyIBC.GestionDesUtilisateur.repository.UtilisateurRepository;
import com.app.MyIBC.GestionDesUtilisateur.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilisateurStatService {

    private final UtilisateurService utilisateurService;
}
