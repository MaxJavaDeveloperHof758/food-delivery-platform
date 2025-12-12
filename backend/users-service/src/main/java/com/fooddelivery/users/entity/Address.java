package com.fooddelivery.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Address information for managing deliveries")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Address unique identifier",example = "1")
    private Long id;

    @Column(nullable = false,length = 50)
    @Schema(description = "The name of street",example = "Main street",requiredMode = Schema.RequiredMode.REQUIRED)
    private String street;

    @Column(nullable = false,length = 50)
    @Schema(description = "The name of city",example = "Boston",requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;

    @Column(nullable = false,length = 25)
    @Schema(description = "The zip code",example = "02989",requiredMode = Schema.RequiredMode.REQUIRED)
    private String zip;

    @Column(nullable = false,length = 100)
    @Schema(description = "The name of state",example = "Massachusetts",requiredMode = Schema.RequiredMode.REQUIRED)
    private String state;

    @Column(nullable = false,length = 50)
    @Schema(description = "The name of country",example = "United States",requiredMode = Schema.RequiredMode.REQUIRED)
    private String country;

    @ManyToOne
    @JoinColumn(name="user_id")
    @Schema(description = "User who owns this address",implementation = User.class)
    private User user;

}
