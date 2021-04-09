package com.tannat.country.services;

import com.tannat.country.dtos.CountryDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CountryService {

    CountryDto getById(Long id);

    List<CountryDto> getAll();

    List<CountryDto> getPage(Pageable pageable);

    List<CountryDto> getPageFiltered(Pageable pageable, String searchText);

    CountryDto add(CountryDto c);

    CountryDto update(CountryDto c);

    CountryDto patch(CountryDto countryPatch);

    void deleteById(Long id);
}
