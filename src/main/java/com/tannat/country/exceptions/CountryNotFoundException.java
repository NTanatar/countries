package com.tannat.country.exceptions;

public class CountryNotFoundException extends ResourceNotFoundException {

    public CountryNotFoundException(String message) {
        super(message);
    }

    public CountryNotFoundException(Long id) {
        super("Country with id " + id + " not found");
    }
}
