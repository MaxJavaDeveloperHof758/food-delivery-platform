package com.fooddelivery.orders.mapper;

import com.fooddelivery.orders.dto.OrderRequestDto;
import com.fooddelivery.orders.dto.OrderResponseDto;
import com.fooddelivery.orders.dto.OrderSlimDto;
import com.fooddelivery.orders.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "items", source = "items")
    @Mapping(target = "payment", ignore = true)
    @Mapping(target = "statusUpdatedAt", source = "statusUpdatedAt")
    OrderResponseDto orderToOrderResponseDto(Order order);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "status",ignore = true)
    @Mapping(target = "orderDate",ignore = true)
    @Mapping(target = "userId",ignore = true)
    @Mapping(target = "totalPrice",ignore = true)
    @Mapping(target = "items",ignore = true)
    @Mapping(target = "payment",ignore = true)
    @Mapping(target = "deliveryAddressId", source = "deliveryAddressId")
    Order orderRequestDtoToOrder(OrderRequestDto orderRequestDto);

    default Page<OrderResponseDto> pageOrdersToPageOrdersDto(Page<Order> page) {
        return page.map(this::orderToOrderResponseDto);
    }

    @Mapping(target = "deliveryAddressId", ignore = true)
    @Mapping(target = "restaurantId", ignore = true)
    OrderSlimDto orderToOrderSlimDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
    List<OrderSlimDto> ordersToOrderSlimDtos(List<Order> orders);

    default Order toOrderWithUserId(OrderRequestDto requestDto, Long userId) {
        Order order = orderRequestDtoToOrder(requestDto);
        order.setUserId(userId);
        return order;
    }

}
