package com.tannat.country.services.impl;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
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
    private JpaCityRepository cityRepository;
    @Mock
    private JpaCountryRepository countryRepository;

    @InjectMocks
    private CountryServiceImpl countryService;

    private final Country COUNTRY_F = Country.builder()
            .id(45L).name("Forest").regionsCount(4).governmentType("monarchy").isLandlocked(false).build();

    private final Country COUNTRY_O = Country.builder()
            .id(72L).name("Ocean").regionsCount(1).governmentType("-").isLandlocked(false).build();

    private final List<City> CITIES_F = Arrays.asList(
            City.builder().id(3L).name("Hill").countryId(COUNTRY_F.getId()).build(),
            City.builder().id(5L).name("Lake").countryId(COUNTRY_F.getId()).build());

    private final List<City> CITIES_O = Collections.singletonList(
            City.builder().id(11L).name("Atlantida").countryId(COUNTRY_O.getId()).build());

    private final Country CREATED_CO = Country.builder().id(102L).name("Created").build();
    private final Country UPDATED_CO = Country.builder().id(103L).name("Updated").build();

    private final City CREATED_CI = City.builder().id(22L).countryId(102L).name("CreatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_existingWithoutCities() {
        long existingId = 45L;
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(COUNTRY_F), result);
        verify(countryRepository).findById(existingId);
        verify(cityRepository).getAllByCountryId(existingId);
    }

    @Test
    void getById_existingWithCities() {
        long existingId = 45L;
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));
        when(cityRepository.getAllByCountryId(existingId)).thenReturn(CITIES_F);

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(COUNTRY_F, CITIES_F), result);
        verify(countryRepository).findById(existingId);
        verify(cityRepository).getAllByCountryId(existingId);
    }

    @Test
    void getById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 70L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.getById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
        verify(cityRepository, never()).getAllByCountryId(anyLong());
    }

    @Test
    void getAll() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(COUNTRY_F, COUNTRY_O));
        when(cityRepository.getAllByCountryId(COUNTRY_F.getId())).thenReturn(CITIES_F);
        when(cityRepository.getAllByCountryId(COUNTRY_O.getId())).thenReturn(CITIES_O);

        List<CountryDto> result = countryService.getAll();

        assertEquals(Arrays.asList(new CountryDto(COUNTRY_F, CITIES_F), new CountryDto(COUNTRY_O, CITIES_O)), result);
        verify(countryRepository).findAll();
        verify(cityRepository).getAllByCountryId(COUNTRY_F.getId());
        verify(cityRepository).getAllByCountryId(COUNTRY_O.getId());
    }

    @Test
    void getPage_noParameters() {
        //TODO
    }

    @Test
    void getPage_secondPageSortByName() {
        //TODO
    }

    @Test
    void add_withCity() {
        Country inputCountry = Country.builder().name("Ireland").build();
        when(countryRepository.save(inputCountry)).thenReturn(CREATED_CO);
        City inputCity = City.builder().name("D").build();
        City cityToAdd = City.builder().name("D").countryId(CREATED_CO.getId()).build();
        when(cityRepository.save(cityToAdd)).thenReturn(CREATED_CI);

        CountryDto result = countryService.add(new CountryDto(inputCountry, Collections.singletonList(inputCity)));

        assertEquals(new CountryDto(CREATED_CO, Collections.singletonList(CREATED_CI)), result);
        verify(countryRepository).save(inputCountry);
        verify(cityRepository).save(cityToAdd);
    }

    @Test
    void add_withoutCity() {
        Country input = Country.builder().name("Ireland").build();
        when(countryRepository.save(input)).thenReturn(CREATED_CO);

        CountryDto result = countryService.add(new CountryDto(input, null));

        assertEquals(new CountryDto(CREATED_CO, Collections.emptyList()), result);
        verify(countryRepository).save(input);
    }

    @Test
    void update_existingReplacingCities() {
        Long existingId = COUNTRY_F.getId();
        Country inputCountry = Country.builder().id(existingId).name("Ireland").build();
        City inputCity = City.builder().name("Dub").build();
        City cityToAdd = City.builder().name("Dub").countryId(existingId).build();
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));
        when(countryRepository.save(inputCountry)).thenReturn(UPDATED_CO);
        when(cityRepository.save(cityToAdd)).thenReturn(CREATED_CI);

        CountryDto result = countryService.update(new CountryDto(inputCountry, Collections.singletonList(inputCity)));

        assertEquals(new CountryDto(UPDATED_CO, Collections.singletonList(CREATED_CI)), result);
        verify(countryRepository).findById(existingId);
        verify(countryRepository).save(inputCountry);
        verify(cityRepository).deleteAllByCountryId(existingId);
        verify(cityRepository).save(cityToAdd);
    }

    @Test
    void update_existingDeletingCites() {
        Long existingId = COUNTRY_F.getId();
        Country inputCountry = Country.builder().id(existingId).name("Rain forest").build();
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));
        when(countryRepository.save(inputCountry)).thenReturn(UPDATED_CO);

        CountryDto result = countryService.update(new CountryDto(inputCountry, Collections.emptyList()));

        assertEquals(new CountryDto(UPDATED_CO, Collections.emptyList()), result);
        verify(countryRepository).findById(existingId);
        verify(countryRepository).save(inputCountry);
        verify(cityRepository).deleteAllByCountryId(existingId);
    }

    @Test
    void update_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 19L;
        Country inputCountry = Country.builder().id(nonExistingId).name("Mountain").build();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.update(new CountryDto(inputCountry, Collections.emptyList())));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
        verify(countryRepository, never()).save(inputCountry);
        verify(cityRepository, never()).deleteAllByCountryId(anyLong());
    }

    @Test
    void deleteById_existing() {
        Long existingId = 45L;
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        countryService.deleteById(existingId);

        verify(countryRepository).findById(existingId);
        verify(countryRepository).deleteById(existingId);
        verify(cityRepository).deleteAllByCountryId(existingId);
    }

    @Test
    void deleteById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 6L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.deleteById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
        verify(countryRepository, never()).deleteById(nonExistingId);
        verify(cityRepository, never()).deleteAllByCountryId(nonExistingId);
    }
}