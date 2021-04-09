package com.tannat.country.dtos;

import com.tannat.country.domain.AuditableEntity;
import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import lombok.*;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CountryDto extends AuditableEntity {
    private Long id;
    private String name;
    private String worldRegion;
    private String governmentType;
    private Integer regionsCount;
    private Boolean isLandlocked;
    private LocalDate foundingDate;
    private List<CityDto> cities;

    public CountryDto(Country country) {
        super(country.getCreated(), country.getModified(), country.getCreatedBy(), country.getModifiedBy());
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

    public static void patch(Country country, CountryDto patch) {
        if (patch.getName() != null) {
            country.setName(patch.getName());
        }
        if (patch.getWorldRegion() != null) {
            country.setWorldRegion(patch.getWorldRegion());
        }
        if (patch.getGovernmentType() != null) {
            country.setGovernmentType(patch.getGovernmentType());
        }
        if (patch.getRegionsCount() != null) {
            country.setRegionsCount(patch.getRegionsCount());
        }
        if (patch.getIsLandlocked() != null) {
            country.setIsLandlocked(patch.getIsLandlocked());
        }
        if (patch.getFoundingDate() != null) {
            country.setFoundingDate(patch.getFoundingDate());
        }
    }
}
