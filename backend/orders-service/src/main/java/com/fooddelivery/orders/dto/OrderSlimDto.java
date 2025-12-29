package com.fooddelivery.orders.dto;

import com.fooddelivery.orders.entity.OrderStatus;
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
@Schema(description = "Short response model for the order")
public class OrderSlimDto {
    @Schema(description = "Order unique identifier",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Order status",example = "PLACED",accessMode = Schema.AccessMode.READ_ONLY)
    private OrderStatus status;

    @Schema(description = "User unique identifier",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long userId;

    @Schema(description = "The total sum for the order that must be paid",
            example = "45.90",accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal totalPrice;
}
