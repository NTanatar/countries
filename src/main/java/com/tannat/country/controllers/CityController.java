package com.tannat.country.controllers;


import com.tannat.country.dtos.CityDto;
import com.tannat.country.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/countries")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/cities/{id}")
    public CityDto getById(@PathVariable Long id) {
        return cityService.getById(id);
    }

    @GetMapping("/cities")
    public List<CityDto> getPage(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer sortBy) {
        return cityService.getPage(pageNumber, pageSize, sortBy);
    }

    @GetMapping("/{countryId}/cities")
    public List<CityDto> getByCountry(@PathVariable Long countryId) {
        return cityService.getByCountryId(countryId);
    }

    @PostMapping("/{countryId}/cities")
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto add(
            @PathVariable Long countryId,
            @RequestBody CityDto city) {
        city.setCountryId(countryId);
        return cityService.add(city);
    }

    @PutMapping("/{countryId}/cities/{id}")
    public CityDto update(
            @PathVariable Long countryId,
            @PathVariable Long id,
            @RequestBody CityDto city) {
        city.setCountryId(countryId);
        city.setId(id);
        return cityService.update(city);
    }

    @DeleteMapping("/cities/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        cityService.deleteById(id);
    }
}
