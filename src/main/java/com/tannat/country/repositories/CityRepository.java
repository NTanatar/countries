package com.tannat.country.repositories;

import com.tannat.country.domain.City;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityRepository {

    Optional<City> getById(Long id);

    List<City> getAll();

    Optional<City> add(City c);

    Optional<City> update(City c);

    void deleteById(Long id);

    Set<String> getNamesOfMegaCities();
}
