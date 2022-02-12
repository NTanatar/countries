package com.tannat.country.services.impl.steps;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
import com.tannat.country.services.impl.CityServiceImpl;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CitySteps {

    @Mock
    private JpaCityRepository cityRepository;

    @Mock
    private JpaCountryRepository countryRepository;

    @InjectMocks
    private CityServiceImpl cityService;

    private Country country;
    private City cityToAdd;

    private City cityReturnedByRepo;
    private CityDto cityReturnedByService;


    @Given("there is a country with id $id")
    public void thereIsACountryWithId(Long countryId) {
        MockitoAnnotations.openMocks(this); // is this the right place?

        country = Country.builder().id(countryId).name("Plate").build();
        when(countryRepository.findById(countryId)).thenReturn(Optional.of(country));
    }

    @When("I add a city with name $city to country with id $id")
    public void iAddACity(String cityName, Long countryId) {

        City input = City.builder().name(cityName).build();
        cityToAdd = City.builder().name(cityName).country(country).build();
        cityReturnedByRepo = City.builder().id(555L).country(country).name("CreatedCity").build();
        when(cityRepository.save(cityToAdd)).thenReturn(cityReturnedByRepo);

        cityReturnedByService = cityService.add(countryId, new CityDto(input));
    }

    @Then("the city is added")
    public void theCityIsAdded() {
        assertEquals(new CityDto(cityReturnedByRepo), cityReturnedByService);
        verify(countryRepository).findById(country.getId());
        verify(cityRepository).save(cityToAdd);
    }
}
