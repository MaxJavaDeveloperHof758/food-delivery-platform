package com.fooddelivery.orders.exception;

public class UpdateStatusException extends RuntimeException{
    public UpdateStatusException(String message){
        super(message);
    }
}
