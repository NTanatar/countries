package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ApplicationException;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.CityRepository;
import com.tannat.country.services.CityService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public CityDto getById(@NonNull Long id) {
        return cityRepository.getById(id).map(CityDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("City with id " + id + " not found"));
    }

    @Override
    public List<CityDto> getAll() {
        return cityRepository.getAll().stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getByCountryId(Long countryId) {
        return cityRepository.getByCountryId(countryId).stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public List<CityDto> getPage(Integer pageNumber, Integer pageSize, Integer sortBy) {
        return getPageFiltered(null, pageNumber, pageSize, sortBy);
    }

    @Override
    public List<CityDto> getPageFiltered(String searchText, Integer pageNumber, Integer pageSize, Integer sortBy) {
        PageParameters pageParam = new PageParameters(pageNumber, pageSize, sortBy);
        List<City> page;

        if (StringUtils.hasText(searchText)) {
            page = cityRepository.getPageFiltered(searchText, pageParam);
        } else {
            page = cityRepository.getPage(pageParam);
        }
        return page.stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public CityDto add(@NonNull CityDto c) {
        return cityRepository.add(CityDto.toDomain(c)).map(CityDto::new)
                .orElseThrow(() -> new ApplicationException("Failed to add city " + c));
    }

    @Override
    public CityDto update(@NonNull CityDto c) {
        checkCityExists(c.getId());
        return cityRepository.update(CityDto.toDomain(c)).map(CityDto::new)
                .orElseThrow(() -> new ApplicationException("Failed to update city " + c));
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
