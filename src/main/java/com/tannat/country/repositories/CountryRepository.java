package com.tannat.country.repositories;

import com.tannat.country.domain.Country;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CountryRepository {

    Optional<Country> getById(Long id);

    List<Country> getAll();

    Optional<Country> add(Country c);

    Optional<Country> update(Country c);

    void deleteById(Long id);

    Set<String> getNamesOfRepublics();
}
