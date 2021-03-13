package com.tannat.country.controllers;


import com.tannat.country.dtos.CityDto;
import com.tannat.country.services.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/{id}")
    public CityDto get(@PathVariable Long id) {
        return cityService.getById(id);
    }

    @GetMapping
    public List<CityDto> getAll() {
        return cityService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CityDto add(@RequestBody CityDto city) {
        return cityService.add(city);
    }

    @PutMapping("/{id}")
    public CityDto update(@PathVariable Long id,
                          @RequestBody CityDto city) {
        city.setId(id);
        return cityService.update(city);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        cityService.deleteById(id);
    }
}
