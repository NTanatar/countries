package com.tannat.country.services.impl;

import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ApplicationException;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.CityRepository;
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

    private final CityRepository cityRepository;

    @Override
    public CityDto getById(Long id) {
        return cityRepository.getById(id).map(CityDto::new)
                .orElseThrow(() -> new ResourceNotFoundException("City with id " + id + " not found"));
    }

    @Override
    public List<CityDto> getAll() {
        return cityRepository.getAll().stream().map(CityDto::new).collect(Collectors.toList());
    }

    @Override
    public CityDto add(@NonNull CityDto c) {
        return cityRepository.add(CityDto.toDomain(c)).map(CityDto::new)
                .orElseThrow(() -> new ApplicationException("Failed to add city " + c));
    }

    @Override
    public CityDto update(@NonNull CityDto c) {
        return cityRepository.update(CityDto.toDomain(c)).map(CityDto::new)
                .orElseThrow(() -> new ApplicationException("Failed to update city " + c));
    }

    @Override
    public void deleteById(Long id) {
        cityRepository.deleteById(id);
    }
}
