package com.tannat.country.repositories.impl;

import com.tannat.country.domain.Country;
import com.tannat.country.repositories.CountryRepository;
import com.tannat.country.services.impl.PageParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CountryRepositoryImpl implements CountryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Country> mapper;

    @Override
    public Optional<Country> getById(Long id) {
        try {
            Country result = jdbcTemplate.queryForObject("select * from countries where id = ?", mapper, id);
            return Optional.ofNullable(result);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Country> getAll() {
        return jdbcTemplate.query("select * from countries", mapper);
    }

    @Override
    public List<Country> getPage(PageParameters pageParameters) {
        String sql = "select * from countries order by " + pageParameters.getSortBy() + " limit ? offset ?";
        return jdbcTemplate.query(sql, mapper, pageParameters.getLimit(), pageParameters.getOffset());
    }

    @Override
    public Optional<Country> add(Country c) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> createInsertStatement(conn, c), keyHolder);
        return getById((Long) Objects.requireNonNull(keyHolder.getKeys()).get("id"));
    }

    @Override
    public Optional<Country> update(Country c) {
        jdbcTemplate.update(conn -> createUpdateStatement(conn, c));
        return getById(c.getId());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from countries where id = ?", id);
    }

    @Override
    public Set<String> getNamesOfRepublics() {
        return getAll().stream()
                .filter(country -> country.getGovernmentType().toLowerCase(Locale.ROOT).contains("republic"))
                .map(Country::getName)
                .collect(Collectors.toSet());
    }

    private PreparedStatement createInsertStatement(Connection conn, Country c) throws SQLException {
        String sql = "insert into countries " +
                "(name, world_region, government_type, regions_count, landlocked, founding_date) " +
                "values(?, ?, ?, ?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        fillParameters(ps, c, false);
        return ps;
    }

    private PreparedStatement createUpdateStatement(Connection conn, Country c) throws SQLException {
        String sql = "update countries set " +
                "name = ?, world_region = ?, government_type = ?, regions_count = ?, landlocked = ?, founding_date = ? " +
                "where id = ?";

        PreparedStatement ps = conn.prepareStatement(sql);
        fillParameters(ps, c, true);
        return ps;
    }

    private void fillParameters(PreparedStatement ps, Country c, boolean setId) throws SQLException {
        ps.setString(1, c.getName());
        ps.setString(2, c.getWorldRegion());
        ps.setString(3, c.getGovernmentType());
        ps.setInt(4, c.getRegionsCount());
        ps.setBoolean(5, c.getIsLandlocked());

        Date fd = Optional.ofNullable(c.getFoundingDate()).map(Date::valueOf).orElse(null);
        ps.setDate(6, fd);

        if (setId) {
            ps.setLong(7, c.getId());
        }
    }
}
