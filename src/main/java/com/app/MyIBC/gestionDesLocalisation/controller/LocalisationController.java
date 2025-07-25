package com.app.MyIBC.gestionDesLocalisation.controller;


import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
import com.app.MyIBC.gestionDesLocalisation.repository.DelegationRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.PaysRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/localisations")
@RequiredArgsConstructor
public class LocalisationController {

    private final VilleRepository villeRepository;
    private final PaysRepository paysRepository;
    private final DelegationRepository delegationRepository;


    @GetMapping("/pays")
    public List<Pays> getAllPays() {
        return paysRepository.findAll();
    }

    @PostMapping("/pays")
    public Pays savePays(@RequestBody Pays pays) {
        return paysRepository.save(pays);
    }

    @PostMapping("/ville/{selectedCountryId}")
    public Ville saveVille(@PathVariable Long selectedCountryId, @RequestBody Ville ville) {
        ville.setPays(paysRepository.findById(selectedCountryId).get());
        return villeRepository.save(ville);
    }

    @PostMapping("/delegation/{selectedCityId}")
    public Delegation saveDelegation(@PathVariable Long selectedCityId, @RequestBody Delegation delegation) {
        delegation.setVille(villeRepository.findById(selectedCityId).get());
        return delegationRepository.save(delegation);
    }

    @DeleteMapping("/pays/{id}")
    public void deletePays(@PathVariable Long id) {
        paysRepository.deleteById(id);

    }

    @DeleteMapping("/ville/{id}")
    public void deleteVille(@PathVariable Long id) {
        villeRepository.deleteById(id);
    }

    @DeleteMapping("/delegation/{id}")
    public void deleteDelegation(@PathVariable Long id) {
        delegationRepository.deleteById(id);
    }


}
