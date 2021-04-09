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
import lombok.val;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
        return countryRepository.findById(id).map(CountryDto::new)
                .orElseThrow(() -> new CountryNotFoundException(id));
    }

    @Override
    public List<CountryDto> getAll() {
        return countryRepository.findAll().stream().map(CountryDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> getPage(Pageable pageable) {
        return countryRepository.findAll(pageable)
                .getContent().stream().map(CountryDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CountryDto> getPageFiltered(Pageable pageable, String searchText) {
        if (!StringUtils.hasText(searchText)) {
            return getPage(pageable);
        }
        Country country = Country.builder().name(searchText).worldRegion(searchText).governmentType(searchText).build();
        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withIgnoreCase(true)
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Page<Country> page = countryRepository.findAll(Example.of(country, matcher), pageable);
        return page.getContent().stream().map(CountryDto::new).collect(Collectors.toList());
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
    public CountryDto patch(CountryDto countryPatch) {
        return countryRepository.findById(countryPatch.getId()).map(country -> {
            patchCities(country, countryPatch.getCities());
            CountryDto.patch(country, countryPatch);
            Country updated = countryRepository.save(country);
            return new CountryDto(updated);

        }).orElseThrow(() -> new CountryNotFoundException(countryPatch.getId()));
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
            country.setCities(Collections.emptyList());
        }
        addCities(country, cities);
    }

    private void addCities(Country country, List<CityDto> cities) {
        if (CollectionUtils.isEmpty(cities)) {
            return;
        }
        List<City> result = new ArrayList<>(country.getCities());
        cities.stream().map(CityDto::toDomain).forEach(city -> {
            city.setId(null);
            city.setCountry(country);
            result.add(cityRepository.save(city));
        });
        country.setCities(result);
    }

    private void patchCities(Country country, List<CityDto> cityPatches) {
        if (CollectionUtils.isEmpty(cityPatches)) {
            return;
        }
        val patches = cityPatches.stream()
                .collect(Collectors.partitioningBy(patch -> country.hasCity(patch.getId())));

        patches.get(true).forEach(updatePatch -> {
            country.getCity(updatePatch.getId()).ifPresent(cityToUpdate -> {
                CityDto.patch(cityToUpdate, updatePatch);
                cityRepository.save(cityToUpdate);
            });
        });
        addCities(country, patches.get(false));
    }
}
