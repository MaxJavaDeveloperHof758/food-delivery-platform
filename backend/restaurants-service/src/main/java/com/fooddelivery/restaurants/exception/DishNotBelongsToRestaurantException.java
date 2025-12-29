package com.fooddelivery.restaurants.exception;

public class DishNotBelongsToRestaurantException extends RuntimeException{
    public DishNotBelongsToRestaurantException(String message){
        super(message);
    }
}
