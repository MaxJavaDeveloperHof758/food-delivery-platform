package com.fooddelivery.restaurants.entity;

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
@Table(name="restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,length = 100)
    private String cuisine;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "restaurant",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    @Builder.Default
    private List<Dish> dishes=new ArrayList<>();

}
