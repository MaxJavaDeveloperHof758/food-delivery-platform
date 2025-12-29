package com.fooddelivery.orders.integration.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String fullName;

}
