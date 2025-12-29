package com.fooddelivery.orders.integration.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishResponseDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Long restaurantId;
}
