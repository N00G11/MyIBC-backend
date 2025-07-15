package com.app.MyIBC.StatistiqueEtExport.controller;


import com.app.MyIBC.Authentification.repository.UserRepository;
import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.entity.Participant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import com.app.MyIBC.StatistiqueEtExport.dto.PercentageStatDTO;
import com.app.MyIBC.StatistiqueEtExport.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/statistique")
@RequiredArgsConstructor
public class StatistiqueController {

    private final DirigeantRepository dirigeantRepository;
    private final ParticipantRepository participantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final StatService statService;
    private final UserRepository userRepository;
    private final CampRepository campRepository;

    @GetMapping("/admin/NumberallInscripion")
    public Long getAllInscription(){
        return inscriptionRepository.count();
    }

    @GetMapping("/admin/NumberAllPays")
    public Long getAllPays() {
        return Stream.concat(
                        dirigeantRepository.findAll().stream().map(Dirigeant::getPays),
                        participantRepository.findAll().stream().map(Participant::getPays)
                )
                .filter(Objects::nonNull)
                .distinct()
                .count();
    }

    @GetMapping("/admin/allCamp")
    public Long getAllCamp(){
        return campRepository.count();
    }

    @GetMapping("/admin/totalAmount")
    public Long getTotalAmount(){
        return inscriptionRepository.findAll().stream().mapToLong(c -> c.getCamp().getPrix()).sum();
    }

    @GetMapping("/admin/totalAmountTransport")
    public Long getTotalAmountTransport(){
        return statService.getTotalAmountForTransportAll();
    }



    @GetMapping("/admin/repartition")
    public Map<String, List<PercentageStatDTO>> getRepartitionStats() {
        var inscriptions = inscriptionRepository.findAll();
        long total = inscriptions.size();

        if (total == 0) return Map.of();

        // Par type de camp
        Map<String, Long> countByCamp = inscriptions.stream()
                .collect(Collectors.groupingBy(i -> i.getCamp().getType(), Collectors.counting()));

        // Par sexe
        Map<String, Long> countBySexe = inscriptions.stream()
                .collect(Collectors.groupingBy(i -> i.getParticipant().getSexe(), Collectors.counting()));

        // Par pays
        Map<String, Long> countByPays = inscriptions.stream()
                .collect(Collectors.groupingBy(i -> i.getParticipant().getPays(), Collectors.counting()));

        // Par ville
        Map<String, Long> countByVille = inscriptions.stream()
                .collect(Collectors.groupingBy(i -> i.getParticipant().getVille(), Collectors.counting()));

        return Map.of(
                "parCamp", toPercentageDTO(countByCamp, total),
                "parSexe", toPercentageDTO(countBySexe, total),
                "parPays", toPercentageDTO(countByPays, total),
                "parVille", toPercentageDTO(countByVille, total)
        );
    }

    private List<PercentageStatDTO> toPercentageDTO(Map<String, Long> counts, long total) {
        return counts.entrySet().stream()
                .map(e -> new PercentageStatDTO(e.getKey(), (int)(e.getValue() * 100 / total)))
                .collect(Collectors.toList());
    }

}
