package com.tannat.country.services;

import com.tannat.country.dtos.CityDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CityService {

    CityDto getById(Long id);

    List<CityDto> getAll();

    List<CityDto> getByCountryId(Long countryId);

    List<CityDto> getPage(Pageable pageable);

    List<CityDto> getPageFiltered(Pageable pageable, String searchText);

    CityDto add(Long countryId, CityDto city);

    CityDto update(Long countryId, CityDto city);

    void deleteById(Long id);
}
