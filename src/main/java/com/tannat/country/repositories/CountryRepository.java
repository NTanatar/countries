package com.tannat.country.repositories;

import com.tannat.country.domain.Country;
import com.tannat.country.services.impl.PageParameters;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CountryRepository {

    Optional<Country> getById(Long id);

    List<Country> getAll();

    List<Country> getPage(PageParameters pageParameters);

    Optional<Country> add(Country c);

    Optional<Country> update(Country c);

    void deleteById(Long id);

    Set<String> getNamesOfRepublics();
}
