package com.fooddelivery.users.exception;

public class AddressNotFoundException extends RuntimeException{
    public AddressNotFoundException(String message){
        super(message);
    }
}
