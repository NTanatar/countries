package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @InjectMocks
    private CityServiceImpl cityService;

    private final City CITY_A = City.builder().id(1L).name("A").population(456).build();
    private final City CITY_B = City.builder().id(2L).name("B").population(11).build();
    private final City CREATED = City.builder().id(22L).name("CreatedCity").build();
    private final City UPDATED = City.builder().id(23L).name("UpdatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(cityRepository.getAllByCountryId(countryId)).thenReturn(Arrays.asList(CITY_A, CITY_B));

        List<CityDto> result = cityService.getByCountryId(countryId);

        assertEquals(Arrays.asList(new CityDto(CITY_A), new CityDto(CITY_B)), result);
        verify(cityRepository).getAllByCountryId(countryId);
    }

    @Test
    void getPage() {
        //TODO
    }

    @Test
    void getPageFiltered() {
        //TODO
    }

    @Test
    void add() {
        City input = City.builder().name("Town").build();
        when(cityRepository.save(input)).thenReturn(CREATED);

        CityDto result = cityService.add(new CityDto(input));

        assertEquals(new CityDto(CREATED), result);
        verify(cityRepository).save(input);
    }

    @Test
    void update_existing() {
        Long existingId = 1L;
        City input = City.builder().name("Dorf").id(existingId).build();
        when(cityRepository.findById(existingId)).thenReturn(Optional.of(CITY_A));
        when(cityRepository.save(input)).thenReturn(UPDATED);

        CityDto result = cityService.update(new CityDto(input));

        assertEquals(new CityDto(UPDATED), result);
        verify(cityRepository).findById(existingId);
        verify(cityRepository).save(input);
    }

    @Test
    void update_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 33L;
        City input = City.builder().name("Dorf").id(nonExistingId).build();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.update(new CityDto(input)));

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