package com.tengy.delivery_fee_calculator.exceptions;

public class RequestException extends RuntimeException {
    public RequestException(String message){
       super(message);
    }
}
