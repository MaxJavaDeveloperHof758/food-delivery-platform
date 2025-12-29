package com.fooddelivery.restaurants.entity;

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
@Table(name = "dishes")
@Schema(description = "Dish model associated to the restaurant menu")
public class Dish {
    @Schema(description = "Dish unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "The name of dish", example = "Truffle Carbonara", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String name;

    @Schema(description = "The description of the dish in restaurant's menu",
            example = "Creamy pasta with pancetta,pecorino cheese,egg yolk, and a hint of black truffle",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String description;

    @Schema(description = "The price for the unit of dish", example = "20", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal price;

    @Schema(description = "URL of the dish's image",
            example = "https://images.unsplash.com/photo-1598866594230-a7c12756260f?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "image_url",nullable = true, length = 500)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

}
