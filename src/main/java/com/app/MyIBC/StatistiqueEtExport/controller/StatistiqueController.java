package com.app.MyIBC.StatistiqueEtExport.controller;

import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionInscription.entity.Inscription;
import com.app.MyIBC.GestionInscription.repository.InscriptionRepository;
import com.app.MyIBC.GestionPayement.entity.Payement;
import com.app.MyIBC.GestionPayement.repository.PayementRepository;
import com.app.MyIBC.StatistiqueEtExport.dto.PercentageStatDTO;
import com.app.MyIBC.gestionDesLocalisation.repository.PaysRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/statistique")
@RequiredArgsConstructor
public class StatistiqueController {

    private final InscriptionRepository inscriptionRepository;
    private final PayementRepository payementRepository;
    private final CampRepository campRepository;
    private final PaysRepository paysRepository;

    @GetMapping("/admin/NumberallInscripion")
    public Long getAllInscription() {
        return inscriptionRepository.count();
    }

    @GetMapping("/admin/NumberAllPays")
    public Long getAllPays() {
        return paysRepository.count();
    }

    @GetMapping("/admin/allCamp")
    public Long getAllCamp() {
        return campRepository.count();
    }

    @GetMapping("/admin/totalAmount")
    public Long getTotalAmount() {
        return inscriptionRepository.findAll().stream()
                .mapToLong(i -> i.getCamp().getPrix())
                .sum();
    }

    // ✅ NOUVEL ENDPOINT : total amount pour un camp spécifique
    @GetMapping("/admin/totalAmountByCamp/{id}")
    public ResponseEntity<Long> getTotalAmountByCamp(@PathVariable Long id) {
        Camp camp = campRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camp non trouvé"));

        // On récupère tous les paiements associés à ce camp et on somme les montants
        Long total = payementRepository.findAll().stream()
                .filter(p -> p.getCamp() != null && p.getCamp().getId().equals(id))
                .mapToLong(Payement::getMontant)
                .sum();

        return ResponseEntity.ok(total);
    }

    @GetMapping("/admin/repartition")
    public Map<String, List<PercentageStatDTO>> getRepartitionStats() {
        var inscriptions = inscriptionRepository.findAll();
        long total = inscriptions.size();

        if (total == 0) return Map.of();

        Map<String, Long> countByCamp = inscriptions.stream()
                .collect(Collectors.groupingBy(i -> i.getCamp().getType(), Collectors.counting()));

        Map<String, Long> countBySexe = inscriptions.stream()
                .collect(Collectors.groupingBy(Inscription::getSexe, Collectors.counting()));

        Map<String, Long> countByPays = inscriptions.stream()
                .collect(Collectors.groupingBy(Inscription::getPays, Collectors.counting()));

        Map<String, Long> countByVille = inscriptions.stream()
                .collect(Collectors.groupingBy(Inscription::getVille, Collectors.counting()));

        return Map.of(
                "parCamp", toPercentageDTO(countByCamp, total),
                "parSexe", toPercentageDTO(countBySexe, total),
                "parPays", toPercentageDTO(countByPays, total),
                "parVille", toPercentageDTO(countByVille, total)
        );
    }

    private List<PercentageStatDTO> toPercentageDTO(Map<String, Long> counts, long total) {
        return counts.entrySet().stream()
                .map(e -> new PercentageStatDTO(e.getKey(), (int) (e.getValue() * 100 / total)))
                .collect(Collectors.toList());
    }
}
