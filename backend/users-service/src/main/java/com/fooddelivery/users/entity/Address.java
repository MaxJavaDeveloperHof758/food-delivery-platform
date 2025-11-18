package com.fooddelivery.users.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 50)
    private String street;

    @Column(nullable = false,length = 50)
    private String city;

    @Column(nullable = false,length = 25)
    private String zip;

    @Column(nullable = false,length = 100)
    private String state;

    @Column(nullable = false,length = 50)
    private String country;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
