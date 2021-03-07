package com.tannat.country.repositories.impl;

import com.tannat.country.domain.City;
import com.tannat.country.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CityRepositoryImpl implements CityRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<City> mapper;

    @Override
    public Optional<City> getById(Long id) {
        try {
            City result = jdbcTemplate.queryForObject("select * from cities where id = ?", mapper, id);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<City> getAll() {
        return jdbcTemplate.query("select * from cities", mapper);
    }

    @Override
    public Optional<City> add(City c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> createInsertStatement(conn, c), keyHolder);
        return getById((Long) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
    }

    @Override
    public Optional<City> update(City c) {
        jdbcTemplate.update(conn -> createUpdateStatement(conn, c));
        return getById(c.getId());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from cities where id = ?", id);
    }

    @Override
    public Set<String> getNamesOfMegaCities() {
        return getAll().stream()
                .filter(city -> city.getPopulation() > 1_000_000)
                .map(City::getName)
                .collect(Collectors.toSet());
    }

    private PreparedStatement createInsertStatement(Connection conn, City c) throws SQLException {
        String sql = "insert into cities " +
                "(name, country_id, founding_date, city_day, has_river, population) " +
                "values (?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        fillParameters(ps, c, false);
        return ps;
    }

    private PreparedStatement createUpdateStatement(Connection conn, City c) throws SQLException {
        String sql = "update cities set " +
                "name = ?, country_id = ?, founding_date = ?, city_day = ?, has_river = ?, population = ?" +
                "where id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        fillParameters(ps, c, true);
        return ps;
    }

    private void fillParameters(PreparedStatement ps, City c, boolean setId) throws SQLException {
        ps.setString(1, c.getName());
        ps.setLong(2, c.getCountryId());

        Date fd = Optional.ofNullable(c.getFoundingDate()).map(Date::valueOf).orElse(null);
        ps.setDate(3, fd);

        Date cd = Optional.ofNullable(c.getCityDay()).map(Date::valueOf).orElse(null);
        ps.setDate(4, cd);

        ps.setBoolean(5, c.getHasRiver());
        ps.setInt(6, c.getPopulation());
        if (setId) {
            ps.setLong(7, c.getId());
        }
    }

}

