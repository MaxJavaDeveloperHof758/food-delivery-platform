package com.fooddelivery.orders.exception;

public class RefundPaymentException extends RuntimeException{
    public RefundPaymentException(String message){
        super(message);
    }
}
