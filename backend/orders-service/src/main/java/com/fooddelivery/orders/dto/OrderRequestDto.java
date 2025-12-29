package com.fooddelivery.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Order model for creating/updating order with items")
public class OrderRequestDto {

    @Schema(description = "Restaurant unique identifier",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @Schema(description = "Delivery address identifier",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;

    @NotEmpty(message = "Order must consist of at least one item")
    @Valid
    private List<OrderItemRequestDto> items;
}
