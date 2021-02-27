package com.tannat.country.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Country {
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;
    private Boolean isLandlocked;
    private LocalDate foundingDate;
}
