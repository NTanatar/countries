package com.tannat.country.dtos;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CountryDto {
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;
    private Boolean isLandlocked;
    private LocalDate foundingDate;
    private List<CityDto> cities;

    public CountryDto(Country country) {
        this.id = country.getId();
        this.name = country.getName();
        this.worldRegion = country.getWorldRegion();
        this.governmentType = country.getGovernmentType();
        this.regionsCount = country.getRegionsCount();
        this.isLandlocked = country.getIsLandlocked();
        this.foundingDate = country.getFoundingDate();
        this.cities = CollectionUtils.isEmpty(country.getCities()) ?
                Collections.emptyList() :
                country.getCities().stream().map(CityDto::new).collect(Collectors.toList());
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
        List<City> cities = CollectionUtils.isEmpty(dto.getCities()) ?
                Collections.emptyList() :
                dto.getCities().stream().map(CityDto::toDomain).collect(Collectors.toList());
        country.setCities(cities);
        return country;
    }
}
