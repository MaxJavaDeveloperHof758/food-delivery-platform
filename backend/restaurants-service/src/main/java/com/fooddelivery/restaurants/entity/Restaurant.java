package com.fooddelivery.restaurants.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurants")
@Schema(description = "Restaurant model")
public class Restaurant {
    @Schema(description = "Restaurant's unique identifier", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Restaurant's unique name", example = "White Lotus", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, unique = true)
    private String name;

    @Schema(description = "Restaurant's cuisine", example = "European, Fusion", requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false, length = 100)
    private String cuisine;

    @Schema(description = "Restaurant's address",
            example = "70 Nevsky Prospect, Saint Petersburg, Russia",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Dish> dishes = new ArrayList<>();
}
