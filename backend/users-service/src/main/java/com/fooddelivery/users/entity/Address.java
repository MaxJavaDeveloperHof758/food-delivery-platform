package com.fooddelivery.users.entity;

import jakarta.persistence.*;

@Entity
@Table(name="addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String city;

    private String zip;

    private String state;

    private String country;

    @OneToOne
    @JoinColumn(name="user_id",unique = true)
    private User user;

}
