package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CityServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private final City cityA = City.builder().id(1L).name("A").population(456).build();
    private final City cityB = City.builder().id(2L).name("B").population(11).build();
    private final City createdCity = City.builder().id(22L).name("CreatedCity").build();
    private final City updatedCity = City.builder().id(23L).name("UpdatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_existing() {
        Long existingId = 1L;
        when(cityRepository.getById(existingId)).thenReturn(Optional.of(cityA));

        CityDto result = cityService.getById(existingId);

        assertEquals(new CityDto(cityA), result);
        verify(cityRepository).getById(existingId);
    }

    @Test
    void getById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 70L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.getById(nonExistingId));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).getById(nonExistingId);
    }

    @Test
    void getAll() {
        when(cityRepository.getAll()).thenReturn(Arrays.asList(cityA, cityB));

        List<CityDto> result = cityService.getAll();

        assertEquals(Arrays.asList(new CityDto(cityA), new CityDto(cityB)), result);
        verify(cityRepository).getAll();
    }

    @Test
    void getByCountryId() {
        Long countryId = 8L;
        when(cityRepository.getByCountryId(countryId)).thenReturn(Arrays.asList(cityA, cityB));

        List<CityDto> result = cityService.getByCountryId(countryId);

        assertEquals(Arrays.asList(new CityDto(cityA), new CityDto(cityB)), result);
        verify(cityRepository).getByCountryId(countryId);
    }

    @Test
    void getPage() {
        when(cityRepository.getPage(new PageParameters(1, 2, 3)))
                .thenReturn(Collections.singletonList(cityB));

        List<CityDto> result = cityService.getPage(1, 2, 3);

        assertEquals(Collections.singletonList(new CityDto(cityB)), result);
        verify(cityRepository).getPage(new PageParameters(1, 2, 3));
    }

    @Test
    void getPageFiltered() {
        when(cityRepository.getPageFiltered("A", new PageParameters(0, 2, 3)))
                .thenReturn(Collections.singletonList(cityA));

        List<CityDto> result = cityService.getPageFiltered("A", 0, 2, 3);

        assertEquals(Collections.singletonList(new CityDto(cityA)), result);
        verify(cityRepository).getPageFiltered("A", new PageParameters(0, 2, 3));
    }

    @Test
    void add() {
        City input = City.builder().name("Town").build();
        when(cityRepository.add(input)).thenReturn(Optional.of(createdCity));

        CityDto result = cityService.add(new CityDto(input));

        assertEquals(new CityDto(createdCity), result);
        verify(cityRepository).add(input);
    }

    @Test
    void update_existing() {
        Long existingId = 1L;
        City input = City.builder().name("Dorf").id(existingId).build();
        when(cityRepository.getById(existingId)).thenReturn(Optional.of(cityA));
        when(cityRepository.update(input)).thenReturn(Optional.of(updatedCity));

        CityDto result = cityService.update(new CityDto(input));

        assertEquals(new CityDto(updatedCity), result);
        verify(cityRepository).getById(existingId);
        verify(cityRepository).update(input);
    }

    @Test
    void update_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 33L;
        City input = City.builder().name("Dorf").id(nonExistingId).build();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.update(new CityDto(input)));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).getById(nonExistingId);
        verify(cityRepository, never()).update(input);
    }

    @Test
    void deleteById_existing() {
        Long existingId = 1L;
        when(cityRepository.getById(existingId)).thenReturn(Optional.of(cityA));

        cityService.deleteById(existingId);

        verify(cityRepository).getById(existingId);
        verify(cityRepository).deleteById(existingId);
    }

    @Test
    void deleteById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 6L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> cityService.deleteById(nonExistingId));

        assertEquals("City with id " + nonExistingId + " not found", e.getMessage());
        verify(cityRepository).getById(nonExistingId);
        verify(cityRepository, never()).deleteById(anyLong());
    }
}