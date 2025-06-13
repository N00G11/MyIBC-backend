package com.app.MyIBC.GestionDesCamps.entity;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Camp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String type;
    private String trancheAge;
    private Long prix;
    private String description;
    private Long participants = 0L;
}
