package com.app.MyIBC.GestionPayement.controller;


import com.app.MyIBC.GestionPayement.Service.PayementService;
import com.app.MyIBC.GestionPayement.entity.Payement;
import com.app.MyIBC.GestionPayement.repository.PayementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payement")
@RequiredArgsConstructor
public class PayementController {

    private final PayementService payementService;
    private final PayementRepository payementRepository;

    @GetMapping("/all")
    public List<Payement> getAll(){
        return payementRepository.findAll();
    }

    @PostMapping("/add/{campType}/{code}/{tresorierCode}")
    public Payement savePayement(@PathVariable String campType, @PathVariable String code, @PathVariable String tresorierCode){
        return payementService.savePayement(campType, code, tresorierCode);
    }
}
