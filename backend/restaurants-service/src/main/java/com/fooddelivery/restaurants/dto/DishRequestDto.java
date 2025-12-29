package com.fooddelivery.restaurants.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dish model for creating/updating dish in restaurant's menu")
public class DishRequestDto {
    @NotBlank(message = "Name of dish cannot be empty")
    @Schema(description = "The name of dish", example = "Truffle Carbonara", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Description of dish cannot be empty")
    @Schema(description = "The description of the dish in restaurant's menu",
            example = "Creamy pasta with pancetta,pecorino cheese,egg yolk, and a hint of black truffle",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @Digits(integer = 8, fraction = 2, message = "Price must have max 8 integer and 2 fraction digits")
    @Schema(description = "The price for the unit of dish", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal price;

}
