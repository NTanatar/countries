package com.tannat.country.repositories;

import com.tannat.country.domain.City;

import java.util.List;

public interface CityRepository {

    City getById(Long id);

    List<City> getAll();

    City add(City c);

    City update(City c);

    void deleteById(Long id);
}
