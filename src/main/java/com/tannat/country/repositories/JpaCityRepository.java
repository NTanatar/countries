package com.tannat.country.repositories;

import com.tannat.country.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaCityRepository extends JpaRepository<City, Long> {

    List<City> getAllByCountryId(Long id);

    void deleteAllByCountryId(Long id);
}
