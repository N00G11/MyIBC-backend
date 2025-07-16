package com.app.MyIBC.StatistiqueEtExport.controller;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesUtilisateur.entity.Dirigeant;
import com.app.MyIBC.GestionDesUtilisateur.repository.DirigeantRepository;
import com.app.MyIBC.GestionDesUtilisateur.repository.ParticipantRepository;
import com.app.MyIBC.InscriptionEtAssignation.entity.Inscription;
import com.app.MyIBC.InscriptionEtAssignation.repository.InscriptionRepository;
import com.app.MyIBC.StatistiqueEtExport.service.DirigeantStatSercvice;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistique/dirigeant")
@RequiredArgsConstructor
public class DirigeantStatController {

    private final DirigeantStatSercvice dirigeantStatSercvice;
    private final DirigeantRepository dirigeantRepository;
    private final ParticipantRepository participantRepository;
    private final InscriptionRepository inscriptionRepository;
    private final CampRepository campRepository;

    @GetMapping("/allParticipantsCount/{email}")
    public Long getallParticipantscount(@PathVariable String email){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return (long) dirigeantStatSercvice.getAllInscriptionByDirigeant(dirireant).size();
    }

    @GetMapping("/allParticipants/{email}")
    public List<Inscription> getallParticipants(@PathVariable String email){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return dirigeantStatSercvice.getAllInscriptionByDirigeant(dirireant);
    }

    @GetMapping("/allCamp")
    public List<Camp> getAllCamp(){
        return campRepository.findAll();
    }

    @GetMapping("/totalAmount/{email}")
    public Long getallAmount(@PathVariable String email){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return dirigeantStatSercvice.getTotalAmount(dirireant);
    }

    @GetMapping("/totalAmountforTransport/{email}")
    public Long getallAmountForTransport(@PathVariable String email){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return dirigeantStatSercvice.getTotalAmountForTransport(dirireant);
    }

    @GetMapping("/totalAmountByCamp/{email}/{id}")
    public Long getallAmountByCamp(@PathVariable String email, @PathVariable Long id){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return dirigeantStatSercvice.getTotalAmountByCamp(dirireant, id);
    }

    @GetMapping("/numberParticipantsByCamp/{email}/{id}")
    public Long getnumberParticipantsByCamp(@PathVariable String email, @PathVariable Long id){
        Dirigeant dirireant = dirigeantRepository.findByEmail(email);
        return dirigeantStatSercvice.getNumberParticipantsByCamp(dirireant, id);
    }

    @GetMapping("/email/{email}")
    public Dirigeant getDirigeantByEmail(@PathVariable String email) {
        return dirigeantRepository.findByEmail(email);
    }

}
