package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
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

    private final JpaCityRepository cityRepository;
    private final JpaCountryRepository countryRepository;

    @Override
    public CountryDto getById(@NonNull Long id) {
        return countryRepository.findById(id).map(this::getCitiesAndConvert)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.findAll().stream().map(this::getCitiesAndConvert).collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy) {
        return getAll(); //TODO
    }

    @Override
    public CountryDto add(@NonNull CountryDto c) {
        c.setId(null);
        Country added = countryRepository.save(CountryDto.toDomain(c));
        return new CountryDto(added, updateCities(c.getCities(), added.getId()));
    }

    @Override
    public CountryDto update(@NonNull CountryDto c) {
        return countryRepository.findById(c.getId()).map(country -> {
            country.setName(c.getName());
            country.setWorldRegion(c.getWorldRegion());
            country.setGovernmentType(c.getGovernmentType());
            country.setRegionsCount(c.getRegionsCount());
            country.setIsLandlocked(c.getIsLandlocked());
            country.setFoundingDate(c.getFoundingDate());
            Country updated = countryRepository.save(country);
            return new CountryDto(updated, updateCities(c.getCities(), c.getId()));
        }).orElseThrow(() -> new ResourceNotFoundException("Country with id " + c.getId() + " not found"));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        checkCountryExists(id);
        cityRepository.deleteAllByCountryId(id);
        countryRepository.deleteById(id);
    }

    private CountryDto getCitiesAndConvert(Country c) {
        return new CountryDto(c, cityRepository.getAllByCountryId(c.getId()));
    }

    private List<City> updateCities(List<CityDto> cities, Long countryId) {
        cityRepository.deleteAllByCountryId(countryId);

        if (CollectionUtils.isEmpty(cities)) {
            return Collections.emptyList();
        }
        return cities.stream()
                .peek(city -> city.setCountryId(countryId))
                .peek(city -> city.setId(null))
                .map(city -> cityRepository.save(CityDto.toDomain(city)))
                .collect(Collectors.toList());
    }

    private void checkCountryExists(Long id) {
        countryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Country with id " + id + " not found"));
    }
}
