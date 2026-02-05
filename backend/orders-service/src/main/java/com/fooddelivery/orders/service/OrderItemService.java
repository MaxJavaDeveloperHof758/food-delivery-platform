package com.fooddelivery.orders.service;

import com.fooddelivery.orders.dto.OrderItemResponseDto;
import com.fooddelivery.orders.entity.OrderItem;
import com.fooddelivery.orders.entity.OrderStatus;
import com.fooddelivery.orders.exception.ResourceNotFoundException;
import com.fooddelivery.orders.exception.StatusModificationException;
import com.fooddelivery.orders.exception.ValidationException;
import com.fooddelivery.orders.integration.RestaurantServiceClient;
import com.fooddelivery.orders.integration.dto.DishResponseDto;
import com.fooddelivery.orders.mapper.OrderItemMapper;
import com.fooddelivery.orders.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final RestaurantServiceClient restaurantServiceClient;

    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId) {
        log.info("Getting order items for order: {}", orderId);
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemResponseDto> dtos = orderItemMapper.orderItemsToOrderItemResponseDtos(orderItems);
        enrichOrderItemsWithDishNames(dtos);
        return dtos;
    }

    public List<OrderItemResponseDto> getOrderItemsByOrderIdWithOrder(Long orderId) {
        log.info("Getting order items for order: {} with order", orderId);
        List<OrderItem> orderItems = orderItemRepository.findByOrderIdWithOrder(orderId);
        List<OrderItemResponseDto> dtos = orderItemMapper.orderItemsToOrderItemResponseDtos(orderItems);
        enrichOrderItemsWithDishNames(dtos);
        return dtos;
    }

    public OrderItemResponseDto getOrderItemById(Long orderItemId) {
        log.info("Getting order item: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id " + orderItemId));
        OrderItemResponseDto dto = orderItemMapper.orderItemToOrderItemResponseDto(orderItem);
        try {
            DishResponseDto dish = restaurantServiceClient.getDishById(dto.getDishId());
            dto.setDishName(dish != null ? dish.getName() : "Unknown");
        } catch (Exception e) {
            log.warn("Could not fetch dish name for dishId: {}", dto.getDishId(), e);
            dto.setDishName("Unknown");
        }
        return dto;
    }

    public Integer getTotalSoldQuantityByDishId(Long dishId) {
        log.info("Getting total quantity of sold items for dish: {}", dishId);
        return orderItemRepository.getTotalSoldQuantityByDishId(dishId);
    }

    public OrderItemResponseDto updateOrderItemQuantity(Long orderItemId, Integer newQuantity) {
        log.info("Updating quantity for order item: {} to {}", orderItemId, newQuantity);
        if (newQuantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id " + orderItemId));
        OrderStatus currentStatus = orderItem.getOrder().getStatus();
        if (currentStatus.equals(OrderStatus.DELIVERED) || currentStatus.equals(OrderStatus.CANCELLED)) {
            throw new StatusModificationException("Cannot modify order item - order status is " + currentStatus);
        }
        orderItem.setQuantity(newQuantity);
        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.orderItemToOrderItemResponseDto(updatedOrderItem);
    }

    public void deleteOrderItem(Long orderItemId) {
        log.info("Deleting order item: {}", orderItemId);
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Order item not found with id " + orderItemId));
        OrderStatus currentStatus = orderItem.getOrder().getStatus();
        if (currentStatus.equals(OrderStatus.DELIVERED) || currentStatus.equals(OrderStatus.CANCELLED)) {
            throw new StatusModificationException("Cannot delete order item - order status is " + currentStatus);
        }
        orderItemRepository.delete(orderItem);
    }

    private void enrichOrderItemsWithDishNames(List<OrderItemResponseDto> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        List<Long> dishIds = items.stream()
                .map(OrderItemResponseDto::getDishId)
                .distinct()
                .toList();

        Map<Long, String> dishNames = new HashMap<>();
        for (Long dishId : dishIds) {
            try {
                DishResponseDto dish = restaurantServiceClient.getDishById(dishId);
                dishNames.put(dishId, dish != null ? dish.getName() : "Unknown");
            } catch (Exception e) {
                log.warn("Could not fetch dish name for dishId: {}", dishId, e);
                dishNames.put(dishId, "Unknown");
            }
        }
        items.forEach(item -> {
            String dishName = dishNames.get(item.getDishId());
            item.setDishName(dishName != null ? dishName : "Unknown");
        });
    }
}
