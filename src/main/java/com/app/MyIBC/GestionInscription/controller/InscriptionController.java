package com.app.MyIBC.GestionInscription.controller;


import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import com.app.MyIBC.GestionInscription.service.InscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscription")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionRepository inscriptionRepository;
    private final InscriptionService inscriptionService;


    @GetMapping("/all")
    public List<Inscription> getAllInscription(){
        return inscriptionRepository.findAll();
    }


    @GetMapping("/{id}")
    public Inscription getInscriptionById(@PathVariable Long id) {
        return inscriptionRepository.findById(id).get();
    }


    @PostMapping("/add/{code}/{campId}")
    public Inscription saveInscription(@PathVariable String code, @PathVariable Long campId, @RequestBody Inscription inscription){
        return inscriptionService.saveInscription(inscription, code, campId);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteInscription(@PathVariable Long id){
        inscriptionRepository.deleteById(id);
    }
}
