package com.app.MyIBC.gestionDesLocalisation.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Pays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "pays", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ville> villes;
}
