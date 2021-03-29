package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CityServiceImplTest {

    @Mock
    private JpaCityRepository cityRepository;

    @Mock
    private JpaCountryRepository countryRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private final Country COUNTRY = Country.builder().id(40L).name("Finnland").build();

    private final City CITY_A = City.builder().id(1L).country(COUNTRY).name("A").population(456).build();
    private final City CITY_B = City.builder().id(2L).country(COUNTRY).name("B").population(11).build();
    private final City CREATED = City.builder().id(22L).country(COUNTRY).name("CreatedCity").build();
    private final City UPDATED = City.builder().id(23L).country(COUNTRY).name("UpdatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        COUNTRY.setCities(Arrays.asList(CITY_A, CITY_B));
    }

    @Test
    void getById_existing() {
        Long existingId = 1L;
        when(cityRepository.findById(existingId)).thenReturn(Optional.of(CITY_A));

        CityDto result = cityService.getById(existingId);

        assertEquals(new CityDto(CITY_A), result);
        verify(cityRepository).findById(existingId);
    }

    @Test
    void getById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 70L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.getById(nonExistingId));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).findById(nonExistingId);
    }

    @Test
    void getAll() {
        when(cityRepository.findAll()).thenReturn(Arrays.asList(CITY_A, CITY_B));

        List<CityDto> result = cityService.getAll();

        assertEquals(Arrays.asList(new CityDto(CITY_A), new CityDto(CITY_B)), result);
        verify(cityRepository).findAll();
    }

    @Test
    void getByCountryId() {
        Long countryId = 8L;
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(COUNTRY));

        List<CityDto> result = cityService.getByCountryId(countryId);

        assertEquals(Arrays.asList(new CityDto(CITY_A), new CityDto(CITY_B)), result);
        verify(countryRepository).findById(countryId);
    }

    @Test
    void getPage() {
        PageRequest p = PageRequest.of(1, 2, Sort.by("id"));
        when(cityRepository.findAll(p)).thenReturn(new PageImpl<>(Arrays.asList(CITY_A, CITY_B)));

        List<CityDto> result = cityService.getPage(p);

        assertEquals(Arrays.asList(new CityDto(CITY_A), new CityDto(CITY_B)), result);
        verify(cityRepository).findAll(p);
    }

    @Test
    void add() {
        City input = City.builder().name("Town").build();
        City cityToAdd = City.builder().name("Town").country(COUNTRY).build();
        when(countryRepository.findById(COUNTRY.getId())).thenReturn(Optional.of(COUNTRY));
        when(cityRepository.save(cityToAdd)).thenReturn(CREATED);

        CityDto result = cityService.add(COUNTRY.getId(), new CityDto(input));

        assertEquals(new CityDto(CREATED), result);
        verify(countryRepository).findById(COUNTRY.getId());
        verify(cityRepository).save(cityToAdd);
    }

    @Test
    void update_existing() {
        Long existingId = 1L;
        City input = City.builder().id(existingId).name("Dorf").build();
        City cityToUpdate = City.builder().id(existingId).name("Dorf").country(COUNTRY).build();
        when(countryRepository.findById(COUNTRY.getId())).thenReturn(Optional.of(COUNTRY));
        when(cityRepository.findById(existingId)).thenReturn(Optional.of(CITY_A));
        when(cityRepository.save(cityToUpdate)).thenReturn(UPDATED);

        CityDto result = cityService.update(COUNTRY.getId(), new CityDto(input));

        assertEquals(new CityDto(UPDATED), result);
        verify(countryRepository).findById(COUNTRY.getId());
        verify(cityRepository).findById(existingId);
        verify(cityRepository).save(cityToUpdate);
    }

    @Test
    void update_nonExistingCity_ResourceNotFoundException() {
        Long nonExistingId = 33L;
        City input = City.builder().name("Dorf").id(nonExistingId).build();
        when(countryRepository.findById(COUNTRY.getId())).thenReturn(Optional.of(COUNTRY));

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.update(COUNTRY.getId(), new CityDto(input)));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).findById(nonExistingId);
        verify(cityRepository, never()).save(input);
    }

    @Test
    void deleteById_existing() {
        Long existingId = 1L;
        when(cityRepository.findById(existingId)).thenReturn(Optional.of(CITY_A));

        cityService.deleteById(existingId);

        verify(cityRepository).findById(existingId);
        verify(cityRepository).deleteById(existingId);
    }

    @Test
    void deleteById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 6L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.deleteById(nonExistingId));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).findById(nonExistingId);
        verify(cityRepository, never()).deleteById(anyLong());
    }
}