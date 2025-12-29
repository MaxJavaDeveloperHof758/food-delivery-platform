package com.fooddelivery.orders.exception;

public class StatusModificationException extends RuntimeException{
    public StatusModificationException(String message){
        super(message);
    }
}
