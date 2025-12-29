package com.fooddelivery.orders.dto;

import com.fooddelivery.orders.entity.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response model for the order")
public class OrderResponseDto {
    @Schema(description = "Order unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Order status", example = "PLACED", accessMode = Schema.AccessMode.READ_ONLY)
    private OrderStatus status;

    @Schema(description = "The time when order was created",
            example = "2025-10-02:10-00-00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime orderDate;

    @Schema(description = "The time when order status was updated",
            example = "2025-10-05:12-10-05", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime statusUpdatedAt;

    @Schema(description = "User unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "Restaurant unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long restaurantId;

    @Schema(description = "Delivery address identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long deliveryAddressId;

    @Schema(description = "The total sum for the order that must be paid",
            example = "45.90", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalPrice;

    @Schema(description = "The list of items ordered", accessMode = Schema.AccessMode.READ_ONLY)
    private List<OrderItemResponseDto> items;

    @Schema(description = "The details of payment", accessMode = Schema.AccessMode.READ_ONLY)
    private PaymentResponseDto payment;
}
