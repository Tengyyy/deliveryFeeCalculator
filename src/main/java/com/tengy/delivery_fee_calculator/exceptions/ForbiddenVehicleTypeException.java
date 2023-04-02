package com.tengy.delivery_fee_calculator.exceptions;

public class ForbiddenVehicleTypeException extends RuntimeException{

    public ForbiddenVehicleTypeException(){
        super("Usage of selected vehicle type is forbidden");
    }
}
