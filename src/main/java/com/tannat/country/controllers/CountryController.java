package com.tannat.country.controllers;

import com.tannat.country.dtos.CountryDto;
import com.tannat.country.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/countries")
@RequiredArgsConstructor
public class CountryController {

    private final CountryService countryService;

    @GetMapping("/{id}")
    public CountryDto get(@PathVariable Long id) {
        return countryService.getById(id);
    }

    @GetMapping
    public List<CountryDto> getPage(
            @RequestParam(required = false) String filter,
            Pageable pageable) {
        return countryService.getPageFiltered(pageable, filter);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CountryDto add(@RequestBody CountryDto country) {
        return countryService.add(country);
    }

    @PutMapping("/{id}")
    public CountryDto update(@PathVariable Long id,
                             @RequestBody CountryDto country) {
        country.setId(id);
        return countryService.update(country);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        countryService.deleteById(id);
    }
}
