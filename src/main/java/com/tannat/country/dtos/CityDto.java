package com.tannat.country.dtos;

import com.tannat.country.domain.AuditableEntity;
import com.tannat.country.domain.City;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CityDto extends AuditableEntity {
    private Long id;
    private String name;
    private LocalDate foundingDate;
    private LocalDate cityDay;
    private Boolean hasRiver;
    private Integer population;

    public CityDto(City c) {
        super(c.getCreated(), c.getModified(), c.getCreatedBy(), c.getModifiedBy());
        this.id = c.getId();
        this.name = c.getName();
        this.foundingDate = c.getFoundingDate();
        this.cityDay = c.getCityDay();
        this.hasRiver = c.getHasRiver();
        this.population = c.getPopulation();
    }

    public static City toDomain(CityDto dto) {
        City c = new City();
        c.setId(dto.getId());
        c.setName(dto.getName());
        c.setFoundingDate(dto.getFoundingDate());
        c.setCityDay(dto.getCityDay());
        c.setHasRiver(dto.getHasRiver());
        c.setPopulation(dto.getPopulation());
        return c;
    }
}
