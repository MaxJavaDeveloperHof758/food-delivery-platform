package com.fooddelivery.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order item model for creating/updating order item in user's order")
public class OrderItemRequestDto {

    @Schema(description = "Dish unique identifier",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Dish ID is required")
    private Long dishId;

    @Schema(description = "The quantity of an item",example = "5",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "The quantity of an item is required")
    @Min(value = 1,message = "The quantity must be at least 1")
    private Integer quantity;
}
