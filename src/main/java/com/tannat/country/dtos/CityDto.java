package com.tannat.country.dtos;

import com.tannat.country.domain.City;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CityDto {
    private Long id;
    private Long countryId;
    private String name;
    private LocalDate foundingDate;
    private LocalDate cityDay;
    private Boolean hasRiver;
    private Integer population;

    public CityDto(City c) {
        this.id = c.getId();
        this.countryId = c.getCountryId();
        this.name = c.getName();
        this.foundingDate = c.getFoundingDate();
        this.cityDay = c.getCityDay();
        this.hasRiver = c.getHasRiver();
        this.population = c.getPopulation();
    }

    public static City toDomain(CityDto dto) {
        City c = new City();
        c.setId(dto.getId());
        c.setCountryId(dto.getCountryId());
        c.setName(dto.getName());
        c.setFoundingDate(dto.getFoundingDate());
        c.setCityDay(dto.getCityDay());
        c.setHasRiver(dto.getHasRiver());
        c.setPopulation(dto.getPopulation());
        return c;
    }
}
