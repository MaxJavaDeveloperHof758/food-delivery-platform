package com.fooddelivery.users.repository;

import com.fooddelivery.users.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.roles " +
            "LEFT JOIN FETCH u.addresses")
    List<User> findAllWithRolesAndAddresses();

    @Query("SELECT DISTINCT u FROM User u " +
            "LEFT JOIN FETCH u.roles r " +
            "LEFT JOIN FETCH u.addresses a " +
            "WHERE u.id = :id")
    @Transactional(readOnly = true)
    Optional<User> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmailWithRoles(@Param("email") String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findByEmail(String email);

    Optional<User> findByFullName(String fullName);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findUsersByFullNameContaining(@Param("name") String name);
}

