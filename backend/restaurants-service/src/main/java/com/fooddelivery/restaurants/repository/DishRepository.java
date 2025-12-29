package com.fooddelivery.restaurants.repository;

import com.fooddelivery.restaurants.entity.Dish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    Optional<Dish> findByName(String name);

    Page<Dish> findAllByOrderByPriceAsc(Pageable pageable);

    Page<Dish> findByRestaurantId(Long restaurantId, Pageable pageable);
    Dish findByRestaurantIdAndId(Long restaurantId,Long dishId);

    @Query("SELECT CASE WHEN COUNT(d)>0 THEN true ELSE false END " +
            "FROM Dish d WHERE d.restaurant.id=:restaurantId AND d.name=:name")
    Boolean existsByRestaurantIdAndName(@Param("restaurantId") Long restaurantId,
                                        @Param("name") String name);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId " +
            "AND d.price BETWEEN :minPrice AND :maxPrice")
    Page<Dish> findByRestaurantIdAndPriceRange(@Param("restaurantId") Long restaurantId,
                                               @Param("minPrice") Integer minPrice,
                                               @Param("maxPrice") Integer maxPrice,
                                               Pageable pageable);
}
