package com.fooddelivery.orders.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
@Schema(description = "Payment model for storing payment details")
public class Payment {
    @Schema(description = "Payment unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Schema(description = "Payment method for the order",
            example = "CREDIT_CARD",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String method;

    @Schema(description = "The total amount of money that must be paid for the order",
            example = "45.90", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Schema(description = "The status of payment", example = "COMPLETED", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
