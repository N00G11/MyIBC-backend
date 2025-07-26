package com.app.MyIBC.gestionDesLocalisation.controller;

import com.app.MyIBC.gestionDesLocalisation.entities.Delegation;
import com.app.MyIBC.gestionDesLocalisation.entities.Pays;
import com.app.MyIBC.gestionDesLocalisation.entities.Ville;
import com.app.MyIBC.gestionDesLocalisation.repository.DelegationRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.PaysRepository;
import com.app.MyIBC.gestionDesLocalisation.repository.VilleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/localisations")
@RequiredArgsConstructor
public class LocalisationController {

    private final VilleRepository villeRepository;
    private final PaysRepository paysRepository;
    private final DelegationRepository delegationRepository;

    // ---------------- GET ----------------

    @GetMapping("/pays")
    public List<Pays> getAllPays() {
        return paysRepository.findAll();
    }

    // ---------------- CREATE ----------------

    @PostMapping("/pays")
    public Pays savePays(@RequestBody Pays pays) {
        return paysRepository.save(pays);
    }

    @PostMapping("/ville/{selectedCountryId}")
    public Ville saveVille(@PathVariable Long selectedCountryId, @RequestBody Ville ville) {
        Pays pays = paysRepository.findById(selectedCountryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pays non trouvé"));
        ville.setPays(pays);
        return villeRepository.save(ville);
    }

    @PostMapping("/delegation/{selectedCityId}")
    public Delegation saveDelegation(@PathVariable Long selectedCityId, @RequestBody Delegation delegation) {
        Ville ville = villeRepository.findById(selectedCityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville non trouvée"));
        delegation.setVille(ville);
        return delegationRepository.save(delegation);
    }

    // ---------------- UPDATE (modification du nom) ----------------

    @PutMapping("/pays/{id}")
    public Pays updatePaysName(@PathVariable Long id, @RequestBody String newName) {
        Pays pays = paysRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pays non trouvé"));
        pays.setName(newName);
        return paysRepository.save(pays);
    }

    @PutMapping("/ville/{id}")
    public Ville updateVilleName(@PathVariable Long id, @RequestBody String newName) {
        Ville ville = villeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville non trouvée"));
        ville.setName(newName);
        return villeRepository.save(ville);
    }

    @PutMapping("/delegation/{id}")
    public Delegation updateDelegationName(@PathVariable Long id, @RequestBody String newName) {
        Delegation delegation = delegationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Délégation non trouvée"));
        delegation.setName(newName);
        return delegationRepository.save(delegation);
    }

    // ---------------- DELETE ----------------

    @DeleteMapping("/pays/{id}")
    public void deletePays(@PathVariable Long id) {
        if (!paysRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pays non trouvé");
        }
        paysRepository.deleteById(id);
    }

    @DeleteMapping("/ville/{id}")
    public void deleteVille(@PathVariable Long id) {
        if (!villeRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ville non trouvée");
        }
        villeRepository.deleteById(id);
    }

    @DeleteMapping("/delegation/{id}")
    public void deleteDelegation(@PathVariable Long id) {
        if (!delegationRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Délégation non trouvée");
        }
        delegationRepository.deleteById(id);
    }
}
