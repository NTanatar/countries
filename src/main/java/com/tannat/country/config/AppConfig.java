package com.tannat.country.config;

import com.tannat.country.domain.City;
import com.tannat.country.domain.Country;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.Optional;

@Configuration
public class AppConfig {

    @Bean
    public RowMapper<City> cityRowMapper() {
        return (ResultSet result, int rowNum) -> {
            var c = new City();
            c.setId(result.getLong("id"));
            c.setCountryId(result.getLong("country_id"));
            c.setName(result.getString("name"));

            Date fd = result.getDate("founding_date");
            c.setFoundingDate(Optional.ofNullable(fd).map(Date::toLocalDate).orElse(null));

            Date cd = result.getDate("city_day");
            c.setCityDay(Optional.ofNullable(cd).map(Date::toLocalDate).orElse(null));

            c.setHasRiver(result.getBoolean("has_river"));
            c.setPopulation(result.getInt("population"));
            return c;
        };
    }

    @Bean
    public RowMapper<Country> countryRowMapper() {
        return (ResultSet result, int rowNum) -> {
            var c = new Country();
            c.setId(result.getLong("id"));
            c.setName(result.getString("name"));
            c.setWorldRegion(result.getString("world_region"));
            c.setGovernmentType(result.getString("government_type"));
            c.setRegionsCount(result.getInt("regions_count"));
            c.setIsLandlocked(result.getBoolean("landlocked"));

            Date fd = result.getDate("founding_date");
            c.setFoundingDate(Optional.ofNullable(fd).map(Date::toLocalDate).orElse(null));
            return c;
        };
    }
}
