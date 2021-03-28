package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.CountryNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
import com.tannat.country.services.CountryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
        return countryRepository.findById(id).map(CountryDto::new)
                .orElseThrow(() -> new CountryNotFoundException(id));
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.findAll().stream().map(CountryDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy) {
        return getAll(); //TODO
    }

    @Override
    public CountryDto add(@NonNull CountryDto c) {
        c.setId(null);
        Country added = countryRepository.save(CountryDto.toDomain(c));
        addCities(added, c.getCities());
        return new CountryDto(added);
    }

    @Override
    public CountryDto update(@NonNull CountryDto c) {
        return countryRepository.findById(c.getId()).map(country -> {
            replaceCities(country, c.getCities());
            country.setName(c.getName());
            country.setWorldRegion(c.getWorldRegion());
            country.setGovernmentType(c.getGovernmentType());
            country.setRegionsCount(c.getRegionsCount());
            country.setIsLandlocked(c.getIsLandlocked());
            country.setFoundingDate(c.getFoundingDate());
            Country updated = countryRepository.save(country);
            return new CountryDto(updated);

        }).orElseThrow(() -> new CountryNotFoundException(c.getId()));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        Country country = countryRepository.findById(id).orElseThrow(() -> new CountryNotFoundException(id));
        if (!CollectionUtils.isEmpty(country.getCities())) {
            country.getCities().forEach(city -> cityRepository.deleteById(city.getId()));
        }
        countryRepository.deleteById(id);
    }

    private void replaceCities(Country country, List<CityDto> cities) {
        if (!CollectionUtils.isEmpty(country.getCities())) {
            country.getCities().forEach(city -> cityRepository.deleteById(city.getId()));
        }
        addCities(country, cities);
    }

    private void addCities(Country country, List<CityDto> cities) {
        List<City> added = new ArrayList<>();
        if (!CollectionUtils.isEmpty(cities)) {
            cities.stream().map(CityDto::toDomain).forEach(city -> {
                city.setId(null);
                city.setCountry(country);
                added.add(cityRepository.save(city));
            });
        }
        country.setCities(added);
    }
}
