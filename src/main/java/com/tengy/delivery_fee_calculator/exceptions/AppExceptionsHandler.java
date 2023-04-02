package com.tengy.delivery_fee_calculator.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();

        if(errorMessageDescription == null) errorMessageDescription = ex.toString();

        return new ResponseEntity<>(
                errorMessageDescription, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {ForbiddenVehicleTypeException.class})
    public ResponseEntity<Object> handleForbiddenVehicleTypeException(ForbiddenVehicleTypeException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();

        if(errorMessageDescription == null) errorMessageDescription = ex.toString();

        return new ResponseEntity<>(
                errorMessageDescription, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<Object> handleRequestException(RequestException ex, WebRequest request){
        String errorMessageDescription = ex.getLocalizedMessage();

        if(errorMessageDescription == null) errorMessageDescription = ex.toString();

        return new ResponseEntity<>(
                errorMessageDescription, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
