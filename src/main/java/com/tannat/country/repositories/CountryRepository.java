package com.tannat.country.repositories;

import com.tannat.country.domain.Country;

import java.util.List;

public interface CountryRepository {

    Country getById(Long id);

    List<Country> getAll();

    Country add(Country c);

    Country update(Country c);

    void deleteById(Long id);
}
