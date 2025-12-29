package com.fooddelivery.orders.integration;

import com.fooddelivery.orders.integration.dto.DishResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "restaurants-service", url = "${integration.restaurants-service.url}")
public interface RestaurantServiceClient {
    @GetMapping("/api/restaurants/{id}/exists")
    Boolean checkRestaurantExists(@PathVariable("id") Long id);

    @GetMapping("/api/restaurants/{restaurantId}/dishes/{dishId}")
    DishResponseDto getDishByRestaurantAndDishId(@PathVariable("restaurantId") Long restaurantId,
                                                 @PathVariable("dishId") Long dishId);

    @GetMapping("/api/restaurants/{restaurantId}/dishes")
    List<DishResponseDto> getRestaurantMenu(@PathVariable("restaurantId") Long restaurantId);

    @GetMapping("/api/dishes/{id}")
    DishResponseDto getDishById(@PathVariable("id") Long id);
}
