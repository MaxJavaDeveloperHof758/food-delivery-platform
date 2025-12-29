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
@Table(name="order_items")
@Schema(description = "Order item model with item details")
public class OrderItem {
    @Schema(description = "Order item unique identifier",example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Schema(description = "Dish unique identifier",example = "1",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name="dish_id",nullable = false)
    private Long dishId;

    @Schema(description = "The quiantity of the item ordered",example = "10",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private Integer quantity;

    @Schema(description = "The price for the unit of item",example = "5.25",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;
}
