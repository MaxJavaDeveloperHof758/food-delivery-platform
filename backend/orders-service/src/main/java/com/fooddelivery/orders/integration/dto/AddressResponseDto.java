package com.fooddelivery.orders.integration.dto;

import lombok.Data;

@Data
public class AddressResponseDto {
    private Long id;
    private String street;
    private String city;
    private String state;
    private String country;
    private Long userId;
    private boolean isDefault;
}
