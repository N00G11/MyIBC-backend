package com.app.MyIBC.StatistiqueEtExport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PercentageStatDTO {
    private String label;
    private int value; // en pourcentage
}