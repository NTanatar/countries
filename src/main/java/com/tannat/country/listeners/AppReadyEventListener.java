package com.tannat.country.listeners;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import com.tannat.country.repositories.JpaCityRepository;
import com.tannat.country.repositories.JpaCountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Transactional
@RequiredArgsConstructor
public class AppReadyEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppReadyEventListener.class);

    private final JpaCountryRepository countryRepository;
    private final JpaCityRepository cityRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        testCityRepository();
        testCountryRepository();
    }

    private void testCityRepository() {
        Country co = countryRepository.findById(1L).orElse(new Country());

        City city = new City();
        city.setName("Milano");
        city.setCountry(co);
        city.setHasRiver(true);
        city.setCityDay(LocalDate.of(1234, 8, 5));
        city.setPopulation(1_378_689);

        City added = cityRepository.save(city);
        LOGGER.info("+++ Added city: {} ", added);

        added.setHasRiver(false);
        City updated = cityRepository.save(added);
        LOGGER.info("+++ Updated: {} ", updated);

        City gotById = cityRepository.findById(7L)
                .orElseThrow(() -> new RuntimeException("City not found"));
        LOGGER.info("+++ Got by id = 7: {} ", gotById);

        LOGGER.info("+++ Getting all cities:");
        val all = cityRepository.findAll();
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

        Country added = countryRepository.save(co);
        LOGGER.info("+++ Added country: {} ", added);

        added.setIsLandlocked(true);
        Country updated = countryRepository.save(added);
        LOGGER.info("+++ Updated: {} ", updated);

        Country gotById = countryRepository.findById(7L)
                .orElseThrow(() -> new RuntimeException("Country not found"));
        LOGGER.info("+++ Got by id = 7: {} ", gotById);

        LOGGER.info("+++ Getting all countries:");
        val all = countryRepository.findAll();
        all.forEach(c -> LOGGER.info(c.toString()));

        LOGGER.info("+++ Deleting {}: ", added.getName());
        countryRepository.deleteById(added.getId());
        LOGGER.info("+++ Done");
    }
}
