package com.fooddelivery.orders.exception;

public class AddressNotBelongsToUserException extends RuntimeException{
    public AddressNotBelongsToUserException(String message){
        super(message);
    }
}
