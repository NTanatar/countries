package com.tannat.country.repositories;

import com.tannat.country.domain.City;
import com.tannat.country.services.impl.PageParameters;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CityRepository {

    Optional<City> getById(Long id);

    List<City> getAll();

    List<City> getByCountryId(Long countryId);

    List<City> getPage(PageParameters pageParameters);

    List<City> getPageFiltered(String searchText, PageParameters pageParameters);

    Optional<City> add(City c);

    Optional<City> update(City c);

    void deleteById(Long id);

    void deleteByCountryId(Long id);

    Set<String> getNamesOfMegaCities();
}
