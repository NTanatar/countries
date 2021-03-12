package com.tannat.country.services;

import com.tannat.country.dtos.CountryDto;

import java.util.List;

public interface CountryService {

    CountryDto getById(Long id);

    List<CountryDto> getAll();

    CountryDto add(CountryDto c);

    CountryDto update(CountryDto c);

    void deleteById(Long id);
}
