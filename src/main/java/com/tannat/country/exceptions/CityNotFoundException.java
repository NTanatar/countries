package com.tannat.country.exceptions;

public class CityNotFoundException extends ResourceNotFoundException {

    public CityNotFoundException(String message) {
        super(message);
    }

    public CityNotFoundException(Long id) {
        super("City with id " + id + " not found");
    }
}
