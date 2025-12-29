package com.fooddelivery.orders.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.basic.PostgreSQLEnumType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@TypeDef(name = "pgsql_enum", typeClass = PostgreSQLEnumType.class)
@Table(name = "orders")
@Schema(description = "Order model for storing order details")
public class Order {
    @Schema(description = "Order's unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Order's status", example = "COMPLETED", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Schema(description = "The time when order was created",
            example = "2025-10-02:10-00-00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Schema(description = "The time when order was updated",
            example = "202-10-05:12-00-00", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "status_updated_at", nullable = false)
    private LocalDateTime statusUpdatedAt;

    @Schema(description = "User's unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Schema(description = "Restaurant's unique identifier", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Schema(description = "User's delivery address",
            example = "18 Malaya Bronnaya Street, Moscow, Russia",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "delivery_address_id", nullable = false)
    private Long deliveryAddressId;

    @Schema(description = "The total sum for the order that must be paid",
            example = "45.90",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order")
    @JsonIgnore
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }

    @PreUpdate
    public void updateStatus() {
        this.statusUpdatedAt = LocalDateTime.now();
    }
}


