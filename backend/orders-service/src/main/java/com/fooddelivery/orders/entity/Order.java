package com.fooddelivery.orders.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @Column(name="order_date",nullable = false)
    private LocalDateTime orderDate;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name="restaurant_id",nullable = false)
    private Long restaurantId;

    @Column(name="total_price",nullable = false)
    private Integer totalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderItem> items=new ArrayList<>();

    @OneToOne(mappedBy = "order",cascade = CascadeType.ALL)
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
    }
}
