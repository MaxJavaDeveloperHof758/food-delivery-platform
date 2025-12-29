package com.fooddelivery.orders.exception;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message){
        super(message);
    }
}
