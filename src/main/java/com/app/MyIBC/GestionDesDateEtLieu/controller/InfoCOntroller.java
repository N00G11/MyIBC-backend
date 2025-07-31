package com.app.MyIBC.GestionDesDateEtLieu.controller;


import com.app.MyIBC.GestionDesDateEtLieu.entity.Info;
import com.app.MyIBC.GestionDesDateEtLieu.repository.InfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/info")
@RequiredArgsConstructor
public class InfoCOntroller {

    private final InfoRepository infoRepository;

    @GetMapping
    public ResponseEntity<Info> getInfo() {
        List<Info> infos = infoRepository.findAll();
        if (infos.isEmpty()) {
            return ResponseEntity.ok(null); // ou une valeur par défaut
        }
        return ResponseEntity.ok(infos.get(0));
    }


    @PostMapping
    public Info saveInfo(@RequestBody Info info) {
        return infoRepository.save(info);
    }

    @PutMapping("/{id}")
    public Info updateInfo(@PathVariable Long id,@RequestBody Info info){
        return infoRepository.findById(id).map(i -> {
            i.setLieu(info.getLieu());
            i.setDate(info.getDate());
            return infoRepository.save(i);
        }).get();
    }
}
