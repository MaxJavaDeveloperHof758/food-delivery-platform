package com.fooddelivery.restaurants.mapper;

import com.fooddelivery.restaurants.dto.*;
import com.fooddelivery.restaurants.entity.Dish;
import com.fooddelivery.restaurants.entity.Restaurant;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DishMapper {
    @Mapping(target = "restaurantId", source = "restaurant.id")
    DishResponseDto dishToDishResponseDto(Dish dish);

    @Mapping(target = "restaurant", ignore = true)
    Dish dishRequestDtoToDish(DishRequestDto dishRequestDto);

    default Page<DishResponseDto> pageDishesToPageDishesDto(Page<Dish> page) {
        return page.map(this::dishToDishResponseDto);
    }
    DishSlimDto dishToDishSlimDto(Dish dish);

    List<DishResponseDto> dishesToDishesResponseDTOs(List<Dish> dishes);

    List<DishSlimDto> dishesToDishesSlimDTOs(List<Dish> dishes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    void updateDishFromDto(DishRequestDto dto, @MappingTarget Dish dish);
}
