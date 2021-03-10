package com.tannat.country;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.dtos.CityDto;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.exceptions.ResourceNotFoundException;
import com.tannat.country.services.CityService;
import com.tannat.country.services.CountryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootTest
class CountryApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryApplicationTests.class);

    @Autowired
    private CountryService countryService;

    @Autowired
    private CityService cityService;

    private final Country country = Country.builder().name("Forest").regionsCount(4).governmentType("monarchy").isLandlocked(false).build();

    private final City cityA = City.builder().name("CityA").population(200_000).foundingDate(LocalDate.now()).hasRiver(true).build();
    private final City cityB = City.builder().name("CityB").population(4_000).foundingDate(LocalDate.now().minusYears(1)).hasRiver(false).build();

    @Test
    void testServices() {
        countryService.getAll().forEach(c -> LOGGER.info("-- " + c));

        CountryDto addedCountry = countryService.add(new CountryDto(country, Collections.singletonList(cityA)));
        LOGGER.info("Added country with city A: " + addedCountry);

        cityB.setCountryId(addedCountry.getId());
        CityDto addedCityB = cityService.add(new CityDto(cityB));
        LOGGER.info("Added city B: " + addedCityB);

        LOGGER.info("With 2 cities: " + countryService.getById(addedCountry.getId()));

        addedCountry.setWorldRegion("Oceania");
        LOGGER.info("Updated world region: " + countryService.update(addedCountry));

        countryService.deleteById(addedCountry.getId());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> countryService.getById(addedCountry.getId()));

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> cityService.getById(addedCityB.getId()));
    }

}
