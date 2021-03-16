package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.ApplicationException;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.CityRepository;
import com.tannat.country.repositories.CountryRepository;
import com.tannat.country.services.CountryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Override
    public CountryDto getById(@NonNull Long id) {
        return countryRepository.getById(id).map(this::getCitiesAndConvert)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.getAll().stream().map(this::getCitiesAndConvert).collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy) {
        return countryRepository.getPage(new PageParameters(pageNumber, pageSize, sortBy)).stream()
                .map(this::getCitiesAndConvert).collect(Collectors.toList());
    }

    @Override
    public CountryDto add(@NonNull CountryDto c) {
        Country added = countryRepository.add(CountryDto.toDomain(c))
                .orElseThrow(() -> new ApplicationException("Failed to add country " + c));

        return new CountryDto(added, updateCities(c.getCities(), added.getId()));
    }

    @Override
    public CountryDto update(@NonNull CountryDto c) {
        checkCountryExists(c.getId());
        Country updated = countryRepository.update(CountryDto.toDomain(c))
                .orElseThrow(() -> new ApplicationException("Failed to update country " + c));

        return new CountryDto(updated, updateCities(c.getCities(), updated.getId()));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        checkCountryExists(id);
        cityRepository.deleteByCountryId(id);
        countryRepository.deleteById(id);
    }

    private CountryDto getCitiesAndConvert(Country c) {
        return new CountryDto(c, cityRepository.getByCountryId(c.getId()));
    }

    private List<City> updateCities(List<CityDto> cities, Long countryId) {
        cityRepository.deleteByCountryId(countryId);

        if (CollectionUtils.isEmpty(cities)) {
            return Collections.emptyList();
        }
        return cities.stream()
                .peek(city -> city.setCountryId(countryId))
                .map(city -> cityRepository.add(CityDto.toDomain(city))
                        .orElseThrow(() -> new ApplicationException("Failed to add city " + city)))
                .collect(Collectors.toList());
    }

    private void checkCountryExists(Long id) {
        countryRepository.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
    }
}
