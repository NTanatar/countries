package com.tannat.country.controllers;

import com.tannat.country.dtos.CityDto;
import com.tannat.country.dtos.CountryDto;
import com.tannat.country.services.CityService;
import com.tannat.country.services.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/pages")
@RequiredArgsConstructor
public class PageController {

    private final CityService cityService;
    private final CountryService countryService;

    @GetMapping("/cities/{pageNumber}")
    public List<CityDto> getCityPage(
            @PathVariable Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Integer sortBy) {
        return cityService.getPage(pageNumber, pageSize, sortBy);
    }

    @GetMapping("/countries/{pageNumber}")
    public List<CountryDto> getCountryPage(
            @PathVariable Integer pageNumber,
            @RequestParam Integer pageSize,
            @RequestParam(required = false) Integer sortBy) {
        return countryService.getPage(pageNumber, pageSize, sortBy);
    }
}
