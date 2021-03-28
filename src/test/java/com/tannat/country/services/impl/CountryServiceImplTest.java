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
            City.builder().id(3L).name("Hill").country(COUNTRY_F).build(),
            City.builder().id(5L).name("Lake").country(COUNTRY_F).build());

    private final List<City> CITIES_O = Collections.singletonList(
            City.builder().id(11L).name("Atlantida").country(COUNTRY_O).build());

    private final Country CREATED_CO = Country.builder().id(102L).name("Created").build();
    private final Country UPDATED_CO = Country.builder().id(103L).name("Updated").build();

    private final City CREATED_CI = City.builder().id(22L).country(CREATED_CO).name("CreatedCity").build();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        COUNTRY_F.setCities(CITIES_F);
        COUNTRY_O.setCities(CITIES_O);
    }

    @Test
    void getById_existingWithoutCities() {
        long existingId = 9L;
        Country country = Country.builder().id(9L).name("U").regionsCount(1).governmentType("-").isLandlocked(false).build();
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(country));

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(country), result);
        verify(countryRepository).findById(existingId);
    }

    @Test
    void getById_existingWithCities() {
        long existingId = 45L;
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        CountryDto result = countryService.getById(existingId);

        assertEquals(new CountryDto(COUNTRY_F), result);
        verify(countryRepository).findById(existingId);
    }

    @Test
    void getById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 70L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.getById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
    }

    @Test
    void getAll() {
        when(countryRepository.findAll()).thenReturn(Arrays.asList(COUNTRY_F, COUNTRY_O));

        List<CountryDto> result = countryService.getAll();

        assertEquals(Arrays.asList(new CountryDto(COUNTRY_F), new CountryDto(COUNTRY_O)), result);
        verify(countryRepository).findAll();
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
        List<City> inputCities = Collections.singletonList(City.builder().name("D").build());
        Country inputCountry = Country.builder().name("Ireland").cities(inputCities).build();

        CREATED_CO.setCities(Collections.singletonList(CREATED_CI));
        when(countryRepository.save(inputCountry)).thenReturn(CREATED_CO);

        City cityToAdd = City.builder().name("D").country(CREATED_CO).build();
        when(cityRepository.save(cityToAdd)).thenReturn(CREATED_CI);

        CountryDto result = countryService.add(new CountryDto(inputCountry));

        assertEquals(new CountryDto(CREATED_CO), result);
        verify(countryRepository).save(inputCountry);
        verify(cityRepository).save(cityToAdd);
    }

    @Test
    void add_withoutCity() {
        CREATED_CO.setCities(Collections.emptyList());
        Country input = Country.builder().name("Ireland").cities(Collections.emptyList()).build();
        when(countryRepository.save(input)).thenReturn(CREATED_CO);

        CountryDto result = countryService.add(new CountryDto(input));

        assertEquals(new CountryDto(CREATED_CO), result);
        verify(countryRepository).save(input);
    }

    @Test
    void update_existingReplacingCities() {
        Long existingId = COUNTRY_F.getId();
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        City inputCity = City.builder().name("Dub").build();
        Country inputCountry = Country.builder().id(existingId).name("Ireland")
                .cities(Collections.singletonList(inputCity)).build();

        City cityToAdd = City.builder().name("Dub").country(COUNTRY_F).build();
        when(cityRepository.save(cityToAdd)).thenReturn(CREATED_CI);

        Country countryToSave = Country.builder().id(existingId).name("Ireland")
                .cities(Collections.singletonList(CREATED_CI)).build();
        when(countryRepository.save(countryToSave)).thenReturn(UPDATED_CO);

        CountryDto result = countryService.update(new CountryDto(inputCountry));

        assertEquals(new CountryDto(UPDATED_CO), result);
        verify(countryRepository).findById(existingId);
        CITIES_F.forEach(city -> verify(cityRepository).deleteById(city.getId()));
        verify(cityRepository).save(cityToAdd);
        verify(countryRepository).save(countryToSave);
    }

    @Test
    void update_existingDeletingCites() {
        Long existingId = COUNTRY_F.getId();
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        Country inputCountry = Country.builder().id(existingId).name("Rain forest").build();
        Country countryToSave = Country.builder().id(existingId).name("Rain forest")
                .cities(Collections.emptyList()).build();
        when(countryRepository.save(countryToSave)).thenReturn(UPDATED_CO);

        CountryDto result = countryService.update(new CountryDto(inputCountry));

        assertEquals(new CountryDto(UPDATED_CO), result);
        verify(countryRepository).findById(existingId);
        COUNTRY_F.getCities().forEach(city -> verify(cityRepository).deleteById(city.getId()));
        verify(countryRepository).save(countryToSave);
    }

    @Test
    void update_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 19L;
        Country inputCountry = Country.builder().id(nonExistingId).name("Mountain").build();

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.update(new CountryDto(inputCountry)));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
        verify(countryRepository, never()).save(inputCountry);
    }

    @Test
    void deleteById_existing() {
        Long existingId = 45L;
        when(countryRepository.findById(existingId)).thenReturn(Optional.of(COUNTRY_F));

        countryService.deleteById(existingId);

        verify(countryRepository).findById(existingId);
        verify(countryRepository).deleteById(existingId);
    }

    @Test
    void deleteById_nonExisting_ResourceNotFoundException() {
        Long nonExistingId = 6L;

        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class,
                () -> countryService.deleteById(nonExistingId));

        assertEquals("Country with id " + nonExistingId + " not found", e.getMessage());
        verify(countryRepository).findById(nonExistingId);
        verify(countryRepository, never()).deleteById(nonExistingId);
    }
}