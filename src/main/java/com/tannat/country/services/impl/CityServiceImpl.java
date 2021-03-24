package com.tannat.country.services.impl;

import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.services.CityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final JpaCityRepository cityRepository;

    @Override
    public CityDto getById(@NonNull Long id) {
        return cityRepository.findById(id).map(CityDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("City with id " + id + " not found"));
    }

    @Override
    public List<CityDto> getAll() {
        return cityRepository.findAll().stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getByCountryId(Long countryId) {
        return cityRepository.getAllByCountryId(countryId).stream().map(CityDto::new).collect(Collectors.toList());
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
    public CityDto add(@NonNull CityDto c) {
        c.setId(null);
        return new CityDto(cityRepository.save(CityDto.toDomain(c)));
    }

    @Override
    public CityDto update(@NonNull CityDto c) {
        return cityRepository.findById(c.getId()).map(city -> {
            city.setName(c.getName());
            city.setCountryId(c.getCountryId());
            city.setFoundingDate(c.getFoundingDate());
            city.setCityDay(c.getCityDay());
            city.setHasRiver(c.getHasRiver());
            city.setPopulation(c.getPopulation());
            return new CityDto(cityRepository.save(city));
        }).orElseThrow(() -> new ResourceNotFoundException("City with id " + c.getId() + " not found"));
    }

    @Override
    public void deleteById(@NonNull Long id) {
        checkCityExists(id);
        cityRepository.deleteById(id);
    }

    private void checkCityExists(Long id) {
        getById(id);
    }
}
