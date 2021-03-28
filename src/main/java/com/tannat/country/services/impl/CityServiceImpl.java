package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.CityNotFoundException;
import com.tannat.country.exceptions.CountryNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
import com.tannat.country.services.CityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final JpaCityRepository cityRepository;
    private final JpaCountryRepository countryRepository;

    @Override
    public CityDto getById(@NonNull Long id) {
        return cityRepository.findById(id).map(CityDto::new)
                .orElseThrow(() -> new CityNotFoundException(id));
    }

    @Override
    public List<CityDto> getAll() {
        return cityRepository.findAll().stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getByCountryId(Long countryId) {
        return countryRepository.findById(countryId)
                .map(country -> country.getCities().stream().map(CityDto::new).collect(Collectors.toList()))
                .orElseThrow(() -> new CountryNotFoundException(countryId));
    }

    @Override
    public List<CityDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy) {
        return getPageFiltered(null, pageNumber, pageSize, sortBy);
    }

    @Override
    public List<CityDto> getPageFiltered(String searchText, Integer pageNumber, Integer pageSize, Integer sortBy) {
        //TODO
        return getAll();
    }

    @Override
    public CityDto add(Long countryId, @NonNull CityDto city) {
        return countryRepository.findById(countryId).map(country -> {
            City cityToAdd = CityDto.toDomain(city);
            cityToAdd.setId(null);
            cityToAdd.setCountry(country);
            return new CityDto(cityRepository.save(cityToAdd));
        }).orElseThrow(() -> new CountryNotFoundException(countryId));
    }

    @Override
    public CityDto update(Long countryId, @NonNull CityDto city) {
        return countryRepository.findById(countryId).map(country -> {
            City cityToUpdate = cityRepository.findById(city.getId())
                    .orElseThrow(() -> new CityNotFoundException(city.getId()));
            cityToUpdate.setName(city.getName());
            cityToUpdate.setFoundingDate(city.getFoundingDate());
            cityToUpdate.setCityDay(city.getCityDay());
            cityToUpdate.setHasRiver(city.getHasRiver());
            cityToUpdate.setPopulation(city.getPopulation());
            return new CityDto(cityRepository.save(cityToUpdate));
        }).orElseThrow(() -> new CountryNotFoundException(countryId));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(id));
        cityRepository.deleteById(id);
    }
}
