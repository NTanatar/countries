package com.tannat.country.services;

import com.tannat.country.dtos.CityDto;

import java.util.List;

public interface CityService {

    CityDto getById(Long id);

    List<CityDto> getAll();

    List<CityDto> getByCountryId(Long countryId);

    List<CityDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy);

    List<CityDto> getPageFiltered(String searchText, Integer pageNumber, Integer pageSize, Integer sortBy);

    CityDto add(Long countryId, CityDto city);

    CityDto update(Long countryId, CityDto city);

    void deleteById(Long id);
}
