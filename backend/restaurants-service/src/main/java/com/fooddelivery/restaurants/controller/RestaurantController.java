package com.fooddelivery.restaurants.controller;

import com.fooddelivery.restaurants.dto.DishRequestDto;
import com.fooddelivery.restaurants.dto.DishResponseDto;
import com.fooddelivery.restaurants.dto.RestaurantRequestDto;
import com.fooddelivery.restaurants.dto.RestaurantResponseDto;
import com.fooddelivery.restaurants.service.DishService;
import com.fooddelivery.restaurants.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant API", description = "API for managing restaurants")
@SecurityRequirement(name = "bearerAuth")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final DishService dishService;

    @Operation(summary = "Get all restaurants with pagination",
            description = "Returns all restaurants divided by pages",
            parameters = {@Parameter(name = "size", description = "The number of restaurants that are displayed on the page", example = "15", required = false),
                    @Parameter(name = "sort", description = "The name of column used to sort the restaurants", example = "name", required = false),
                    @Parameter(name = "direction", description = "The direction of sorting (ascending/descending)", example = "ASC")})
    @ApiResponse(responseCode = "200", description = "Restaurants found")
    @ApiResponse(responseCode = "404", description = "Restaurants not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<Page<RestaurantResponseDto>> getAllRestaurants(@PageableDefault(size = 10,
            sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(restaurants);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkRestaurantExists(@PathVariable("id") Long id) {
        Boolean ifExists = restaurantService.ifRestaurantExists(id);
        return ResponseEntity.status(HttpStatus.OK).body(ifExists);
    }

    @Operation(summary = "Get restaurant by ID",
            description = "Returns the restaurant by it's ID",
            parameters = {@Parameter(name = "id", description = "The unique identifier of the restaurant", example = "1", required = true)})
    @ApiResponse(responseCode = "200", description = "Restaurant found")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    @Cacheable(value = "restaurants", key = "#id")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable("id") Long id) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.getRestaurantById(id);
        return ResponseEntity.status(HttpStatus.OK).body(restaurantResponseDto);
    }

    @Operation(summary = "Get restaurants by name and (or) cuisine with pagination",
            description = "Searches restaurants by their name and (or) cuisine with pagination",
            parameters = {@Parameter(name = "name", description = "The name of the restaurant", example = "White Lotus", required = false),
                    @Parameter(name = "cuisine", description = "The restaurant's cuisine", example = "European, Fusion", required = false),
                    @Parameter(name = "size", description = "The number of restaurants that are displayed on the page", example = "15", required = false)})
    @ApiResponse(responseCode = "200", description = "Restaurants found")
    @ApiResponse(responseCode = "404", description = "Restaurants not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search")
    public ResponseEntity<Page<RestaurantResponseDto>> searchRestaurants(@RequestParam(required = false) String name,
                                                                         @RequestParam(required = false) String cuisine,
                                                                         @PageableDefault(size = 10) Pageable pageable) {
        Page<RestaurantResponseDto> restaurants = restaurantService.getRestaurantsDynamicFilters(name, cuisine, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(restaurants);
    }

    @Operation(summary = "Create a new restaurant",
            description = "Creates new restaurant")
    @ApiResponse(responseCode = "201", description = "Restaurant was successfully created")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestBody @Valid RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.createRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantResponseDto);
    }

    @Operation(summary = "Update existing restaurant",
            description = "Updates existing restaurant",
            parameters = {@Parameter(name = "id", description = "Restaurant's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Restaurant was successfully updated")
    @ApiResponse(responseCode = "400", description = "Restaurant already exists in the database")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable("id") Long id,
                                                                  @RequestBody @Valid RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto updatedRestaurant = restaurantService.updateRestaurant(id, restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedRestaurant);
    }

    @Operation(summary = "Delete existing restaurant",
            description = "Deletes existing restaurant",
            parameters = {@Parameter(name = "id", description = "Restaurant's unique identifier", example = "1")})
    @ApiResponse(responseCode = "204", description = "Restaurant was successfully deleted")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable("id") Long id) {
        restaurantService.deleteRestaurant(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Get the list of dishes referred to the restaurant sorted by the range of prices",
            description = "Returns restaurant's menu in price range",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "minPrice", description = "Min value of price to sort the list of dishes", example = "25", required = true),
                    @Parameter(name = "maxPrice", description = "Max value of price to sort the list of dishes", example = "100", required = true)})
    @ApiResponse(responseCode = "200", description = "Restaurant's menu was found")
    @ApiResponse(responseCode = "404", description = "Restaurant's menu not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{restaurantId}/dishes/price")
    public ResponseEntity<Page<DishResponseDto>> getRestaurantMenuInPriceRange(@PathVariable("restaurantId") Long restaurantId,
                                                                               @RequestParam Integer minPrice,
                                                                               @RequestParam Integer maxPrice,
                                                                               @PageableDefault(size = 10)
                                                                                       Pageable pageable) {
        Page<DishResponseDto> dishes = dishService.getDishesByRestaurantIdAndPriceRange(restaurantId, minPrice,
                maxPrice, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dishes);
    }

    @Operation(summary = "Get the list of dishes referred to the restaurant",
            description = "Returns restaurant's menu",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Restaurant's menu was found")
    @ApiResponse(responseCode = "404", description = "Restaurant's menu not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{restaurantId}/dishes")
    public ResponseEntity<Page<DishResponseDto>> getRestaurantMenu(@PathVariable("restaurantId") Long restaurantId,
                                                                   @PageableDefault(size = 10)
                                                                           Pageable pageable) {
        Page<DishResponseDto> dishes = dishService.getAllDishesByRestaurantId(restaurantId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dishes);
    }

    @Operation(summary = "Get one dish by it's ID referred to the restaurant",
            description = "Returns restaurant's dish by it's ID",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
            @Parameter(name="dishId",description = "Dish's unique identifier",example = "1")})
    @ApiResponse(responseCode = "200", description = "Restaurant's dish was found")
    @ApiResponse(responseCode = "404", description = "Restaurant's dish not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<DishResponseDto> getDishByRestaurantAndDishId(@PathVariable("restaurantId") Long restaurantId,
                                                                   @PathVariable("dishId") Long dishId) {
        DishResponseDto dish = dishService.getDishByRestaurantIdAndDishId(restaurantId,dishId);
        return ResponseEntity.status(HttpStatus.OK).body(dish);
    }

    @Operation(summary = "Add dish to the restaurant's menu",
            description = "Adds one dish to the restaurant's menu",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1")})
    @ApiResponse(responseCode = "201", description = "Dish was successfully added to the menu")
    @ApiResponse(responseCode = "400", description = "Dish already exists in the menu")
    @ApiResponse(responseCode = "404", description = "Restaurant not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{restaurantId}/dishes")
    public ResponseEntity<DishResponseDto> addDishToRestaurantMenu(@PathVariable("restaurantId") Long restaurantId,
                                                                   @RequestBody @Valid DishRequestDto dishRequestDto) {
        DishResponseDto dish = dishService.createDishForRestaurant(restaurantId, dishRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(dish);
    }

    @Operation(summary = "Update dish in the restaurant's menu",
            description = "Updates existing dish in the restaurant's menu",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "dishId", description = "Dish's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Dish was successfully updated in the menu")
    @ApiResponse(responseCode = "400", description = "Dish already exists in the menu")
    @ApiResponse(responseCode = "404", description = "Restaurant (dish) not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<DishResponseDto> updateDishForRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                                                   @PathVariable("dishId") Long dishId,
                                                                   @RequestBody @Valid DishRequestDto dishRequestDto) {
        DishResponseDto dish = dishService.updateDishForRestaurant(restaurantId, dishId, dishRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(dish);
    }

    @Operation(summary = "Delete dish from the restaurant's menu",
            description = "Deletes existing dish from the restaurant's menu",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "dishId", description = "Dish's unique identifier", example = "1")})
    @ApiResponse(responseCode = "204", description = "Dish was successfully deleted from the menu")
    @ApiResponse(responseCode = "404", description = "Restaurant (dish) not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{restaurantId}/dishes/{dishId}")
    public ResponseEntity<Void> deleteDishFromRestaurant(@PathVariable("restaurantId") Long restaurantId,
                                                         @PathVariable("dishId") Long dishId) {
        dishService.deleteDishFromRestaurant(restaurantId, dishId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
