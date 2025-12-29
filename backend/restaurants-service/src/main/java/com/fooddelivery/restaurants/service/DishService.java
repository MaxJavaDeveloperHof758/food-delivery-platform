package com.fooddelivery.restaurants.service;

import com.fooddelivery.restaurants.dto.DishRequestDto;
import com.fooddelivery.restaurants.dto.DishResponseDto;
import com.fooddelivery.restaurants.entity.Dish;
import com.fooddelivery.restaurants.entity.Restaurant;
import com.fooddelivery.restaurants.exception.*;
import com.fooddelivery.restaurants.mapper.DishMapper;
import com.fooddelivery.restaurants.repository.DishRepository;
import com.fooddelivery.restaurants.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;
    private final ImageStorageService imageStorageService;

    @Transactional
    public DishResponseDto createDishForRestaurant(Long restaurantId, DishRequestDto dishRequestDto) {
        Restaurant existingRestaurant = restaurantRepository.findById(restaurantId).
                orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + restaurantId));
        if (dishRepository.existsByRestaurantIdAndName(restaurantId, dishRequestDto.getName())) {
            throw new ResourceAlreadyExistsException("Dish already exists");
        }
        Dish dish = dishMapper.dishRequestDtoToDish(dishRequestDto);
        dish.setRestaurant(existingRestaurant);
        dish.setImageUrl(null);
        Dish savedRestaurant = dishRepository.save(dish);
        return dishMapper.dishToDishResponseDto(savedRestaurant);
    }

    public Page<DishResponseDto> getAllDishes(Pageable pageable) {
        return dishMapper.pageDishesToPageDishesDto(dishRepository.findAll(pageable));
    }

    public DishResponseDto getDishById(Long id) {
        Dish dish = dishRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + id));
        return dishMapper.dishToDishResponseDto(dish);
    }

    public DishResponseDto getDishByName(String name) {
        Dish dish = dishRepository.findByName(name).
                orElseThrow(() -> new ResourceNotFoundException("Dish not found with name " + name));
        return dishMapper.dishToDishResponseDto(dish);
    }

    public Page<DishResponseDto> getDishesByRestaurantIdAndPriceRange(Long restaurantId, Integer minPrice,
                                                                      Integer maxPrice, Pageable pageable) {
        return dishMapper.pageDishesToPageDishesDto(
                dishRepository.findByRestaurantIdAndPriceRange(restaurantId, minPrice, maxPrice, pageable));
    }

    public Page<DishResponseDto> getAllDishesOrderedByPrice(Pageable pageable) {
        return dishMapper.pageDishesToPageDishesDto(dishRepository.findAllByOrderByPriceAsc(pageable));
    }

    public Page<DishResponseDto> getAllDishesByRestaurantId(Long restaurantId, Pageable pageable) {
        return dishMapper.pageDishesToPageDishesDto(dishRepository.findByRestaurantId(restaurantId, pageable));
    }
    public DishResponseDto getDishByRestaurantIdAndDishId(Long restaurantId,Long dishId){
        return dishMapper.dishToDishResponseDto(dishRepository.findByRestaurantIdAndId(restaurantId,dishId));
    }

    public DishResponseDto updateDishForRestaurant(Long restaurantId, Long dishId, DishRequestDto dishRequestDto) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id " + restaurantId);
        }
        Dish existingDish = dishRepository.findById(dishId).
                orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + dishId));
        if (!existingDish.getRestaurant().getId().equals(restaurantId)) {
            throw new DishNotBelongsToRestaurantException("Dish doesn't belong to restaurant");
        }
        dishMapper.updateDishFromDto(dishRequestDto, existingDish);
        Dish updatedDish = dishRepository.save(existingDish);
        return dishMapper.dishToDishResponseDto(updatedDish);
    }

    public void deleteDishFromRestaurant(Long restaurantId, Long dishId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new ResourceNotFoundException("Restaurant not found with id " + restaurantId);
        }
        Dish dish = dishRepository.findById(dishId).
                orElseThrow(() -> new ResourceNotFoundException("Dish not found with id " + dishId));
        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new DishNotBelongsToRestaurantException("Cannot delete: dish doesn't belong to restaurant");
        }
        dishRepository.deleteById(dishId);
    }
    //Methods to handle images
    @Transactional
    public DishResponseDto uploadDishImage(Long dishId, MultipartFile imageFile) {
        try {
            Dish dish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));
            if (dish.getImageUrl() != null) {
                imageStorageService.deleteImage(dish.getImageUrl());
            }
            String imagePath = imageStorageService.storeImage(imageFile, "dishes");
            dish.setImageUrl(imagePath);
            Dish savedDish = dishRepository.save(dish);

            return dishMapper.dishToDishResponseDto(savedDish);

        } catch (IOException e) {
            throw new UploadImageFailureException("Failed to upload image: " + e.getMessage());
        }
    }

    @Transactional
    public DishResponseDto removeDishImage(Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found"));

        if (dish.getImageUrl() != null) {
            try {
                imageStorageService.deleteImage(dish.getImageUrl());
                dish.setImageUrl(null);
                Dish savedDish = dishRepository.save(dish);
                return dishMapper.dishToDishResponseDto(savedDish);
            } catch (IOException e) {
                throw new DeleteImageException("Failed to delete image");
            }
        }

        return dishMapper.dishToDishResponseDto(dish);
    }

}
