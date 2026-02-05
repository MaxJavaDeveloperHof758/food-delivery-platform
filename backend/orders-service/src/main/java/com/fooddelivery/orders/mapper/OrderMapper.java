package com.fooddelivery.orders.mapper;

import com.fooddelivery.orders.dto.OrderItemResponseDto;
import com.fooddelivery.orders.dto.OrderRequestDto;
import com.fooddelivery.orders.dto.OrderResponseDto;
import com.fooddelivery.orders.dto.OrderSlimDto;
import com.fooddelivery.orders.entity.Order;
import com.fooddelivery.orders.integration.RestaurantServiceClient;
import com.fooddelivery.orders.integration.dto.DishResponseDto;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper {


    @Mapping(source = "items", target = "items")
    @Mapping(source = "payment", target = "payment")
    OrderResponseDto orderToOrderResponseDto(Order order);

    @AfterMapping
    default void setOrderIdToItems(@MappingTarget OrderResponseDto orderResponseDto, Order order) {
        if (orderResponseDto.getItems() != null && order.getId() != null) {
            orderResponseDto.getItems().forEach(item -> {
                if (item != null) {
                    item.setOrderId(order.getId());
                }
            });
        }
    }


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

    OrderSlimDto orderToOrderSlimDto(Order order);

    List<OrderResponseDto> ordersToOrderResponseDtos(List<Order> orders);
    List<OrderSlimDto> ordersToOrderSlimDtos(List<Order> orders);

    default Order toOrderWithUserId(OrderRequestDto requestDto, Long userId) {
        Order order = orderRequestDtoToOrder(requestDto);
        order.setUserId(userId);
        return order;
    }

}
