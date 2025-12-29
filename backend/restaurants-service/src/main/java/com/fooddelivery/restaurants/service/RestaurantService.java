package com.fooddelivery.restaurants.service;

import com.fooddelivery.restaurants.dto.RestaurantRequestDto;
import com.fooddelivery.restaurants.dto.RestaurantResponseDto;
import com.fooddelivery.restaurants.entity.Restaurant;
import com.fooddelivery.restaurants.exception.ResourceAlreadyExistsException;
import com.fooddelivery.restaurants.exception.ResourceNotFoundException;
import com.fooddelivery.restaurants.mapper.RestaurantMapper;
import com.fooddelivery.restaurants.repository.RestaurantRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Transactional
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        if (restaurantRepository.existsByName(restaurantRequestDto.getName())
                || restaurantRepository.existsByAddress(restaurantRequestDto.getAddress())) {
            throw new ResourceAlreadyExistsException("Restaurant with such name (address) already exists");
        }
        Restaurant restaurant = restaurantMapper.restaurantRequestDtoToRestaurant(restaurantRequestDto);
        if (restaurant.getDishes() == null) {
            restaurant.setDishes(new ArrayList<>());
        }
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.restaurantToRestaurantResponseDto(savedRestaurant);
    }
    public Boolean ifRestaurantExists(Long restaurantId){
        return restaurantRepository.existsById(restaurantId);
    }

    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id " + id));
        return restaurantMapper.restaurantToRestaurantResponseDto(restaurant);
    }

    public Page<RestaurantResponseDto> getAllRestaurants(Pageable pageable) {
        return restaurantMapper.pageRestaurantsToPageRestaurantsDto(restaurantRepository.findAll(pageable));
    }

    public Page<RestaurantResponseDto> getRestaurantsDynamicFilters(String name, String cuisine,
                                                                      Pageable pageable) {
        Specification<Restaurant> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (name != null && !name.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (cuisine != null && !cuisine.trim().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("cuisine")), "%" + cuisine.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return restaurantRepository.findAll(spec, pageable).map(restaurantMapper::restaurantToRestaurantResponseDto);
    }
    @Transactional
    public RestaurantResponseDto updateRestaurant(Long restaurantId,RestaurantRequestDto restaurantRequestDto){
        Restaurant existingRestaurant=restaurantRepository.findById(restaurantId).
                orElseThrow(()->new ResourceNotFoundException("Restaurant not found with id "+restaurantId));
        if(!existingRestaurant.getName().equals(restaurantRequestDto.getName())&&
        restaurantRepository.existsByName(restaurantRequestDto.getName())){
            throw new ResourceAlreadyExistsException("Restaurant with name "+
                    restaurantRequestDto.getName()+" already exists");
        }
        restaurantMapper.updateRestaurantFromDto(restaurantRequestDto,existingRestaurant);
        Restaurant updatedRestaurant=restaurantRepository.save(existingRestaurant);
        return restaurantMapper.restaurantToRestaurantResponseDto(updatedRestaurant);
    }
    @Transactional
    public void deleteRestaurant(Long id){
        if(!restaurantRepository.existsById(id)){
            throw new ResourceNotFoundException("Restaurant not found with id "+id);
        }
        restaurantRepository.deleteById(id);
    }
}
