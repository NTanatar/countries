package com.tannat.country.dtos;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CountryDto {
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;
    private Boolean isLandlocked;
    private LocalDate foundingDate;
    private List<CityDto> cities;

    public CountryDto(Country country, List<City> cities) {
        this.id = country.getId();
        this.name = country.getName();
        this.worldRegion = country.getWorldRegion();
        this.governmentType = country.getGovernmentType();
        this.regionsCount = country.getRegionsCount();
        this.isLandlocked = country.getIsLandlocked();
        this.foundingDate = country.getFoundingDate();
        this.cities = CollectionUtils.isEmpty(cities) ?
                Collections.emptyList() :
                cities.stream().map(CityDto::new).collect(Collectors.toList());
    }

    public CountryDto(Country country) {
        this(country, Collections.emptyList());
    }

    public static Country toDomain(CountryDto dto) {
        Country country = new Country();
        country.setId(dto.getId());
        country.setName(dto.getName());
        country.setWorldRegion(dto.getWorldRegion());
        country.setGovernmentType(dto.getGovernmentType());
        country.setRegionsCount(dto.getRegionsCount());
        country.setIsLandlocked(dto.getIsLandlocked());
        country.setFoundingDate(dto.getFoundingDate());
        return country;
    }
}
