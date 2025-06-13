package com.app.MyIBC.GestionDesCamps.controller;


import com.app.MyIBC.GestionDesCamps.entity.Camp;
import com.app.MyIBC.GestionDesCamps.repository.CampRepository;
import com.app.MyIBC.GestionDesCamps.utils.TypeCamp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/camp")
@RequiredArgsConstructor
public class CampController {

    private final CampRepository campRepository;

    @GetMapping("/all")
    public List<Camp> getAllCamps(){
        return campRepository.findAll();
    }

    @PostMapping("/add")
    public Camp addCamp(@RequestBody Camp camp){
        return campRepository.save(camp);
    }

    @PutMapping("/update/{type}")
    public Optional<Camp> updateCamp(@PathVariable String type, @RequestBody Camp camp){
        return campRepository.findCampByType(type).map(c -> {
            c.setPrix(camp.getPrix());
            return campRepository.save(c);
        });
    }

    @DeleteMapping("/delete/{type}")
    public void deleteCamp(@PathVariable String type){
        campRepository.deleteCampByType(TypeCamp.valueOf(type));
    }
}
