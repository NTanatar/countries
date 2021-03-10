package com.tannat.country.services;

import com.tannat.country.dtos.CityDto;

import java.util.List;

public interface CityService {

    CityDto getById(Long id);

    List<CityDto> getAll();

    CityDto add(CityDto c);

    CityDto update(CityDto c);

    void deleteById(Long id);

}
