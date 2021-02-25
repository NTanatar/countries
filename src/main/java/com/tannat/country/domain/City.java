package com.tannat.country.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class City {
    private Long id;
    private Long countryId;
    private String name;
    private LocalDate foundingDate;
    private LocalDate cityDay;
    private Boolean hasRiver;
    private Integer population;
}
