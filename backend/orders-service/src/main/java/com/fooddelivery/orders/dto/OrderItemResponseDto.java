package com.fooddelivery.orders.dto;

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
@Schema(description = "Response model for order item")
public class OrderItemResponseDto {
    @Schema(description = "Order Item ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Order ID", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long orderId;

    @Schema(description = "Dish unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long dishId;

    @Schema(description = "The quantity for the item chosen", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer quantity;

    @Schema(description = "The price for the unit of item", example = "4.25", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal price;

    @Schema(description = "Dish name", example = "Pizza", accessMode = Schema.AccessMode.READ_ONLY)
    private String dishName;
}
