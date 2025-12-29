package com.fooddelivery.restaurants.mapper;

import com.fooddelivery.restaurants.dto.RestaurantRequestDto;
import com.fooddelivery.restaurants.dto.RestaurantResponseDto;
import com.fooddelivery.restaurants.entity.Restaurant;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DishMapper.class})
public interface RestaurantMapper {
    @Mapping(target = "dishes", source = "dishes")
    RestaurantResponseDto restaurantToRestaurantResponseDto(Restaurant restaurant);

    Restaurant restaurantRequestDtoToRestaurant(RestaurantRequestDto restaurantRequestDto);

    List<RestaurantResponseDto> restaurantsToRestaurantsResponseDTOs(List<Restaurant> restaurants);

    default Page<RestaurantResponseDto> pageRestaurantsToPageRestaurantsDto(Page<Restaurant> page) {
        return page.map(this::restaurantToRestaurantResponseDto);
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    void updateRestaurantFromDto(RestaurantRequestDto dto, @MappingTarget Restaurant restaurant);
}
