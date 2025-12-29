package com.fooddelivery.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response model for dish (short version)")
public class DishSlimDto {
    @Schema(description = "Dish ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Dish name", example = "Truffle Carbonara", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Price for the unit of dish", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal price;
}
