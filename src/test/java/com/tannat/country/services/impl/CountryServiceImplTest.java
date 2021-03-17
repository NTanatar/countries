package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.CityRepository;
import com.tannat.country.repositories.CountryRepository;
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

class CountryServiceImplTest {

    @Mock
    private CityRepository cityRepository;
    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    private final Country countryF = Country.builder()
            .id(45L).name("Forest").regionsCount(4).governmentType("monarchy").isLandlocked(false).build();

    private final Country countryO = Country.builder()
            .id(72L).name("Ocean").regionsCount(1).governmentType("-").isLandlocked(false).build();

    private final List<City> citiesF = Arrays.asList(
            City.builder().id(3L).name("Hill").countryId(countryF.getId()).build(),
            City.builder().id(5L).name("Lake").countryId(countryF.getId()).build());

    private final List<City> citiesO = Collections.singletonList(
            City.builder().id(11L).name("Atlantida").countryId(countryO.getId()).build());

    private final Country createdCountry = Country.builder().id(102L).name("Created").build();
    private final Country updatedCountry = Country.builder().id(103L).name("Updated").build();

    private final City createdCity = City.builder().id(22L).countryId(102L).name("CreatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_existingWithoutCities() {
        long existingId = 45L;
        when(countryRepository.getById(existingId)).thenReturn(Optional.of(countryF));

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(countryF), result);
        verify(countryRepository).getById(existingId);
        verify(cityRepository).getByCountryId(existingId);
    }

    @Test
    void getById_existingWithCities() {
        long existingId = 45L;
        when(countryRepository.getById(existingId)).thenReturn(Optional.of(countryF));
        when(cityRepository.getByCountryId(existingId)).thenReturn(citiesF);

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(countryF, citiesF), result);
        verify(countryRepository).getById(existingId);
        verify(cityRepository).getByCountryId(existingId);
    }

    @Test
    void getById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 70L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.getById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).getById(nonExistingId);
        verify(cityRepository, never()).getByCountryId(anyLong());
    }

    @Test
    void getAll() {
        when(countryRepository.getAll()).thenReturn(Arrays.asList(countryF, countryO));
        when(cityRepository.getByCountryId(countryF.getId())).thenReturn(citiesF);
        when(cityRepository.getByCountryId(countryO.getId())).thenReturn(citiesO);

        List<CountryDto> result = countryService.getAll();

        assertEquals(Arrays.asList(new CountryDto(countryF, citiesF), new CountryDto(countryO, citiesO)), result);
        verify(countryRepository).getAll();
        verify(cityRepository).getByCountryId(countryF.getId());
        verify(cityRepository).getByCountryId(countryO.getId());
    }

    @Test
    void getPage_noParameters() {
        when(countryRepository.getPage(new PageParameters()))
                .thenReturn(Arrays.asList(countryF, countryO));
        when(cityRepository.getByCountryId(countryF.getId())).thenReturn(citiesF);
        when(cityRepository.getByCountryId(countryO.getId())).thenReturn(citiesO);

        List<CountryDto> result = countryService.getPage(null, null, null);

        assertEquals(Arrays.asList(new CountryDto(countryF, citiesF), new CountryDto(countryO, citiesO)), result);
        verify(countryRepository).getPage(new PageParameters());
        verify(cityRepository).getByCountryId(countryF.getId());
        verify(cityRepository).getByCountryId(countryO.getId());
    }

    @Test
    void getPage_secondPageSortByName() {
        when(countryRepository.getPage(new PageParameters(1, 1, 2)))
                .thenReturn(Collections.singletonList(countryO));
        when(cityRepository.getByCountryId(countryO.getId())).thenReturn(citiesO);

        List<CountryDto> result = countryService.getPage(1, 1, 2);

        assertEquals(Collections.singletonList(new CountryDto(countryO, citiesO)), result);
        verify(countryRepository).getPage(new PageParameters(1, 1, 2));
        verify(cityRepository).getByCountryId(countryO.getId());
    }

    @Test
    void add_withCity() {
        Country inputCountry = Country.builder().name("Ireland").build();
        when(countryRepository.add(inputCountry)).thenReturn(Optional.of(createdCountry));
        City inputCity = City.builder().name("D").build();
        City cityToAdd = City.builder().name("D").countryId(createdCountry.getId()).build();
        when(cityRepository.add(cityToAdd)).thenReturn(Optional.of(createdCity));

        CountryDto result = countryService.add(new CountryDto(inputCountry, Collections.singletonList(inputCity)));

        assertEquals(new CountryDto(createdCountry, Collections.singletonList(createdCity)), result);
        verify(countryRepository).add(inputCountry);
        verify(cityRepository).add(cityToAdd);
    }

    @Test
    void add_withoutCity() {
        Country input = Country.builder().name("Ireland").build();
        when(countryRepository.add(input)).thenReturn(Optional.of(createdCountry));

        CountryDto result = countryService.add(new CountryDto(input, null));

        assertEquals(new CountryDto(createdCountry, Collections.emptyList()), result);
        verify(countryRepository).add(input);
    }

    @Test
    void update_existingReplacingCities() {
        Long existingId = 103L;
        Country inputCountry = Country.builder().id(existingId).name("Ireland").build();
        City inputCity = City.builder().name("Dub").build();
        City cityToAdd = City.builder().name("Dub").countryId(updatedCountry.getId()).build();
        when(countryRepository.getById(existingId)).thenReturn(Optional.of(countryF));
        when(countryRepository.update(inputCountry)).thenReturn(Optional.of(updatedCountry));
        when(cityRepository.add(cityToAdd)).thenReturn(Optional.of(createdCity));

        CountryDto result = countryService.update(new CountryDto(inputCountry, Collections.singletonList(inputCity)));

        assertEquals(new CountryDto(updatedCountry, Collections.singletonList(createdCity)), result);
        verify(countryRepository).getById(existingId);
        verify(countryRepository).update(inputCountry);
        verify(cityRepository).deleteByCountryId(existingId);
        verify(cityRepository).add(cityToAdd);
    }

    @Test
    void update_existingDeletingCites() {
        Long existingId = 103L;
        Country inputCountry = Country.builder().id(existingId).name("Rain forest").build();
        when(countryRepository.getById(existingId)).thenReturn(Optional.of(countryF));
        when(countryRepository.update(inputCountry)).thenReturn(Optional.of(updatedCountry));

        CountryDto result = countryService.update(new CountryDto(inputCountry, Collections.emptyList()));

        assertEquals(new CountryDto(updatedCountry, Collections.emptyList()), result);
        verify(countryRepository).getById(existingId);
        verify(countryRepository).update(inputCountry);
        verify(cityRepository).deleteByCountryId(existingId);
    }

    @Test
    void update_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 19L;
        Country inputCountry = Country.builder().id(nonExistingId).name("Mountain").build();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.update(new CountryDto(inputCountry, Collections.emptyList())));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).getById(nonExistingId);
        verify(countryRepository, never()).update(inputCountry);
        verify(cityRepository, never()).deleteByCountryId(anyLong());
    }

    @Test
    void deleteById_existing() {
        Long existingId = 45L;
        when(countryRepository.getById(existingId)).thenReturn(Optional.of(countryF));

        countryService.deleteById(existingId);

        verify(countryRepository).getById(existingId);
        verify(countryRepository).deleteById(existingId);
        verify(cityRepository).deleteByCountryId(existingId);
    }

    @Test
    void deleteById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 6L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.deleteById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).getById(nonExistingId);
        verify(countryRepository, never()).deleteById(nonExistingId);
        verify(cityRepository, never()).deleteByCountryId(nonExistingId);
    }
}