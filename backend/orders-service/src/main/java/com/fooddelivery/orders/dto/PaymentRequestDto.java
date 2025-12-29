package com.fooddelivery.orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Payment model for creating/updating payments")
public class PaymentRequestDto {
    @Schema(description = "Order unique identifier",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Order ID is required")
    private Long orderId;

    @Schema(description = "Payment method",example = "CREDIT_CARD",requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Payment method is required")
    private String method;
}
