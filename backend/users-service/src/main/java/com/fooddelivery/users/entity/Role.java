package com.fooddelivery.users.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roles")
@Schema(description = "Role model for access control")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique role identifier",example = "1",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Role name (should start with ROLE_)",example = "ROLE_USER",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(nullable = false,unique = true)
    private String name;

    @Schema(description = "A set of users with the roles",implementation = User.class)
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    @JsonIgnore
    @Transient
    private Set<User> users=new HashSet<>();
}
