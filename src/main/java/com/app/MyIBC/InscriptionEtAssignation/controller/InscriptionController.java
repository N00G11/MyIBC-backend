package com.app.MyIBC.InscriptionEtAssignation.controller;


import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.InscriptionEtAssignation.entity.Inscription;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import com.app.MyIBC.InscriptionEtAssignation.service.InscriptionService;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/inscription")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService inscriptionService;
    private final InscriptionRepository inscriptionRepository;


    @GetMapping("/all")
    public List<Inscription> getAllInscription(){
        return inscriptionRepository.findAll();
    }


    @GetMapping("/{id}")
    public Inscription getInscriptionById(@PathVariable Long id) {
        return inscriptionRepository.findById(id).get();
    }

    @PostMapping("/add/{campId}")
    public Inscription addInscription(@PathVariable("campId") Long campId,@RequestBody Inscription inscription) throws IOException, WriterException {
        return inscriptionService.assignerDirigeantEquitablement(inscription, campId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteInscription(@PathVariable Long id){
        inscriptionRepository.deleteById(id);
    }
}
