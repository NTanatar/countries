package com.tannat.country.listeners;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.repositories.CityRepository;
import com.tannat.country.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class AppReadyEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppReadyEventListener.class);

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(){
        testCityRepository();
        testCountryRepository();
    }

    private void testCityRepository() {
        City city = new City();
        city.setName("Milano");
        city.setCountryId(1L);
        city.setHasRiver(true);
        city.setCityDay(LocalDate.of(1234, 8, 5));
        city.setPopulation(1_378_689);

        City added = cityRepository.add(city)
                .orElseThrow(() -> new RuntimeException("Could not add city"));
        LOGGER.info("+++ Added city: {} ", added);

        added.setHasRiver(false);
        City updated = cityRepository.update(added)
                .orElseThrow(() -> new RuntimeException("Could not update city"));
        LOGGER.info("+++ Updated: {} ", updated);

        City gotById = cityRepository.getById(7L)
                .orElseThrow(() -> new RuntimeException("City not found"));
        LOGGER.info("+++ Got by id = 7: {} ", gotById);

        LOGGER.info("+++ Getting all cities:");
        val all = cityRepository.getAll();
        all.forEach(c -> LOGGER.info(c.toString()));

        LOGGER.info("+++ Deleting {}: ", added.getName());
        cityRepository.deleteById(added.getId());
        LOGGER.info("+++ Done");
    }

    private void testCountryRepository() {
        Country co = new Country();
        co.setName("San Marino");
        co.setRegionsCount(1);
        co.setIsLandlocked(false);
        co.setGovernmentType("some republic");
        co.setWorldRegion("Europe");

        Country added = countryRepository.add(co)
                .orElseThrow(() -> new RuntimeException("Could not add country"));
        LOGGER.info("+++ Added country: {} ", added);

        added.setIsLandlocked(true);
        Country updated = countryRepository.update(added)
                .orElseThrow(() -> new RuntimeException("Could not update country"));
        LOGGER.info("+++ Updated: {} ", updated);

        Country gotById = countryRepository.getById(7L)
                .orElseThrow(() -> new RuntimeException("Country not found") );
        LOGGER.info("+++ Got by id = 7: {} ", gotById);

        LOGGER.info("+++ Getting all countries:");
        val all = countryRepository.getAll();
        all.forEach(c -> LOGGER.info(c.toString()));

        LOGGER.info("+++ Deleting {}: ", added.getName());
        countryRepository.deleteById(added.getId());
        LOGGER.info("+++ Done");
    }
}
