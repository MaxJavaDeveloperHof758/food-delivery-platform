package com.fooddelivery.restaurants.repository;


import com.fooddelivery.restaurants.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long>, JpaSpecificationExecutor<Restaurant> {
    Boolean existsByName(String name);
    Boolean existsByAddress(String address);
    @Query(value = "SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Restaurant> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    Page<Restaurant> findByCuisine(String cuisine,Pageable pageable);
}
