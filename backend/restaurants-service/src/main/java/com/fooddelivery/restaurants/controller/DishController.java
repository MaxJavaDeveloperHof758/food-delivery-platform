package com.fooddelivery.restaurants.controller;

import com.fooddelivery.restaurants.dto.DishResponseDto;
import com.fooddelivery.restaurants.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/dishes")
@RequiredArgsConstructor
@Tag(name = "Dish API", description = "API for managing restaurant's menu")
public class DishController {
    private final DishService dishService;

    @Operation(summary = "Get all dishes with pagination",
            description = "Returns paginated list of all dishes",
            parameters = {@Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10"),
                    @Parameter(name = "sort", description = "Sorting criteria in format: property(,asc|desc)",example = "name,asc")})
    @ApiResponse(responseCode = "200", description = "Dishes found")
    @ApiResponse(responseCode = "404", description = "Dishes not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping
    public ResponseEntity<Page<DishResponseDto>> getAllDishes(@PageableDefault(size = 10,
            sort = "id",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DishResponseDto> dishes = dishService.getAllDishes(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dishes);
    }

    @Operation(summary = "Get one dish by it's ID",
            description = "Returns one dish by it's ID",
            parameters = {@Parameter(name = "id", description = "Dish's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Dish found")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDto> getDishById(@PathVariable("id") Long id) {
        DishResponseDto dish = dishService.getDishById(id);
        return ResponseEntity.status(HttpStatus.OK).body(dish);
    }

    @Operation(summary = "Get one dish by it's name",
            description = "Returns one dish by it's name",
            parameters = {@Parameter(name = "name", description = "The name of dish", example = "Truffle Carbonara",required = true)})
    @ApiResponse(responseCode = "200", description = "Dish found")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/search/name")
    public ResponseEntity<DishResponseDto> getDishByName(@RequestParam String name) {
        DishResponseDto dish = dishService.getDishByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(dish);
    }

    @Operation(summary = "Get all dishes ordered by price",
            description = "Returns all dishes ordered by price")
    @ApiResponse(responseCode = "200", description = "Dishes found")
    @ApiResponse(responseCode = "404", description = "Dishes not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/sorted/price")
    public ResponseEntity<Page<DishResponseDto>> getAllDishesOrderedByPrice(@PageableDefault(size = 10,
            sort = "price",
            direction = Sort.Direction.ASC) Pageable pageable) {
        Page<DishResponseDto> dishes = dishService.getAllDishesOrderedByPrice(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(dishes);
    }

    @Operation(summary = "Upload dish image",
            description = "Uploads an image for a dish",
    parameters = {@Parameter(name = "id",description = "Dish's unique identifier",example = "1"),
            @Parameter(description = "Image file (JPEG, PNG, GIF, max 5MB)",required = true)})
    @ApiResponse(responseCode = "200", description = "Image successfully uploaded")
    @ApiResponse(responseCode = "400", description = "Invalid file or size exceeded")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PostMapping("/{id}/image")
    public ResponseEntity<DishResponseDto> uploadDishImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        DishResponseDto updatedDish = dishService.uploadDishImage(id, file);
        return ResponseEntity.ok(updatedDish);
    }

    @Operation(summary = "Remove image from existing dish",
            description = "Removes image from the existing dish",
            parameters = {@Parameter(name = "id",description = "Dish's unique identifier",example = "1")})
    @ApiResponse(responseCode = "200", description = "Image was successfully deleted")
    @ApiResponse(responseCode = "404", description = "Dish not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{id}/image")
    public ResponseEntity<DishResponseDto> removeDishImage(@PathVariable Long id) {
        DishResponseDto updatedDish = dishService.removeDishImage(id);
        return ResponseEntity.ok(updatedDish);
    }
}
