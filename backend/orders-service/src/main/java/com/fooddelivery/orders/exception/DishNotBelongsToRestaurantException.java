package com.fooddelivery.orders.exception;

public class DishNotBelongsToRestaurantException extends RuntimeException{
    public DishNotBelongsToRestaurantException(String message){
        super(message);
    }
}
