package com.tannat.country.repositories;

import com.tannat.country.domain.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCityRepository extends JpaRepository<City, Long> {
}
