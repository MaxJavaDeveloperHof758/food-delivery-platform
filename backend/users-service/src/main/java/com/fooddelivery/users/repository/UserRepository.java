package com.fooddelivery.users.repository;

import com.fooddelivery.users.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"roles", "addresses"})
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithDetails(@Param("id") Long id);

    Optional<User> findByEmail(String email);
    Optional<User> findByFullName(String fullName);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT u FROM User u WHERE u.fullName LIKE %:name%")
    List<User> findUsersByFullNameContaining(@Param("name") String name);
}

