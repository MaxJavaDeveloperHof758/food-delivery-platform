package com.fooddelivery.orders.dto;

import com.fooddelivery.orders.entity.PaymentStatus;
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
@Schema(description = "Response model for the payment")
public class PaymentResponseDto {

    @Schema(description = "Payment unique identifier",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Order unique identifier",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long orderId;

    @Schema(description = "Payment method",example = "CREDIT_CARD",accessMode = Schema.AccessMode.READ_ONLY)
    private String method;

    @Schema(description = "The total sum for the payment",example = "45.90",accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal amount;

    @Schema(description = "Payment status",example = "COMPLETED",accessMode = Schema.AccessMode.READ_ONLY)
    private PaymentStatus status;
}
