package com.app.MyIBC.gestionDesLocalisation.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "ville_id")
    @JsonIgnore
    private Pays pays;

    @OneToMany(mappedBy = "ville", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delegation> delegations;
}
