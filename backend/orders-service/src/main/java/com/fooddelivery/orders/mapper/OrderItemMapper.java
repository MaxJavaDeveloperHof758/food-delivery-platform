package com.fooddelivery.orders.mapper;

import com.fooddelivery.orders.dto.OrderItemRequestDto;
import com.fooddelivery.orders.dto.OrderItemResponseDto;
import com.fooddelivery.orders.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {

    @Mapping(target="dishName",ignore = true)
    @Mapping(target = "orderId",source = "order.id")
    OrderItemResponseDto orderItemToOrderItemResponseDto(OrderItem item);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "order",ignore = true)
    @Mapping(target = "price",ignore = true)
    OrderItem orderItemRequestDtoToOrderItem(OrderItemRequestDto orderItemRequestDto);

    @Mapping(target="dishName",ignore=true)
    @Mapping(target = "orderId",source = "order.id")
    List<OrderItemResponseDto> orderItemsToOrderItemResponseDtos(List<OrderItem> items);

    List<OrderItem> orderItemRequestDtosToOrderItems(List<OrderItemRequestDto> items);

}
