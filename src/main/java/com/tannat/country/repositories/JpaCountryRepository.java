package com.tannat.country.repositories;

import com.tannat.country.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCountryRepository extends JpaRepository<Country, Long> {
}
