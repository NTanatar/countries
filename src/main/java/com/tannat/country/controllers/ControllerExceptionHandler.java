package com.tannat.country.controllers;

import com.tannat.country.exceptions.ApplicationException;
import com.tannat.country.exceptions.InvalidParameterException;
import com.tannat.country.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorInfo notFound(Exception e) {
        return new ErrorInfo(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidParameterException.class)
    public ErrorInfo badRequest(Exception e) {
        return new ErrorInfo(e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public ErrorInfo internalError(Exception e) {
        return new ErrorInfo(e.getMessage());
    }
}
