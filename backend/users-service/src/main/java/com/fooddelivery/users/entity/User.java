package com.fooddelivery.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "users")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Schema(description = "User model")
public class User {

    @Schema(description = "User's unique identifier",example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Schema(description = "User's unique email",example = "ivanov1999@mail.ru",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(unique = true, nullable = false, length = 100)
    @EqualsAndHashCode.Include
    private String email;

    @Schema(description = "User's unique password",example = "pass123_word_456",requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Schema(description = "User's name, firstname and lastname",
            example = "Sidorov Ivan Ivanovich",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Column(name = "full_name", nullable = false)
    private String fullName;


    @Schema(description = "The time when user was first created",example = "2025-12-07T10:10:10")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Schema(description = "The time when user was updated",example = "2025-12-07T14:10:25")
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnore
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();
}
