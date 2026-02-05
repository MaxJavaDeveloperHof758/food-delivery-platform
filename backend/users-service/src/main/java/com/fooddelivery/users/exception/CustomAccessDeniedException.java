package com.fooddelivery.users.exception;

public class CustomAccessDeniedException extends RuntimeException{
    public CustomAccessDeniedException(String message){
        super(message);
    }
}
