package com.tannat.country.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Country {
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;
    private Boolean isLandlocked;
    private LocalDate foundingDate;
}
