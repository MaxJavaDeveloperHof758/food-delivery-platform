package com.fooddelivery.orders.service;

import com.fooddelivery.orders.dto.OrderItemResponseDto;
import com.fooddelivery.orders.entity.OrderItem;
import com.fooddelivery.orders.entity.OrderStatus;
import com.fooddelivery.orders.exception.ResourceNotFoundException;
import com.fooddelivery.orders.exception.StatusModificationException;
import com.fooddelivery.orders.exception.ValidationException;
import com.fooddelivery.orders.mapper.OrderItemMapper;
import com.fooddelivery.orders.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public List<OrderItemResponseDto> getOrderItemsByOrderId(Long orderId){
        log.info("Getting order items for order: {}",orderId);
        List<OrderItem> orderItems=orderItemRepository.findByOrderId(orderId);
        return orderItemMapper.orderItemsToOrderItemResponseDtos(orderItems);
    }
    public List<OrderItemResponseDto> getOrderItemsByOrderIdWithOrder(Long orderId){
        log.info("Getting order items for order: {} with order",orderId);
        List<OrderItem> orderItems=orderItemRepository.findByOrderIdWithOrder(orderId);
        return orderItemMapper.orderItemsToOrderItemResponseDtos(orderItems);
    }
    public OrderItemResponseDto getOrderItemById(Long orderItemId){
        log.info("Getting order item: {}",orderItemId);
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new ResourceNotFoundException("Order item not found with id "+orderItemId));
        return orderItemMapper.orderItemToOrderItemResponseDto(orderItem);
    }
    public Integer getTotalSoldQuantityByDishId(Long dishId){
        log.info("Getting total quantity of sold items for dish: {}",dishId);
        return orderItemRepository.getTotalSoldQuantityByDishId(dishId);
    }

    public OrderItemResponseDto updateOrderItemQuantity(Long orderItemId, Integer newQuantity){
        log.info("Updating quantity for order item: {} to {}",orderItemId,newQuantity);
        if(newQuantity<=0){
            throw new ValidationException("Quantity must be positive");
        }
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new ResourceNotFoundException("Order item not found with id "+orderItemId));
        OrderStatus currentStatus=orderItem.getOrder().getStatus();
        if(currentStatus.equals(OrderStatus.COMPLETED)||currentStatus.equals(OrderStatus.CANCELED)){
            throw new StatusModificationException("Cannot modify order item - order status is "+currentStatus);
        }
        orderItem.setQuantity(newQuantity);
        OrderItem updatedOrderItem=orderItemRepository.save(orderItem);
        return orderItemMapper.orderItemToOrderItemResponseDto(updatedOrderItem);
    }
    public void deleteOrderItem(Long orderItemId){
        log.info("Deleting order item: {}",orderItemId);
        OrderItem orderItem=orderItemRepository.findById(orderItemId)
                .orElseThrow(()->new ResourceNotFoundException("Order item not found with id "+orderItemId));
        OrderStatus currentStatus=orderItem.getOrder().getStatus();
        if(currentStatus.equals(OrderStatus.COMPLETED)||currentStatus.equals(OrderStatus.CANCELED)){
            throw new StatusModificationException("Cannot delete order item - order status is "+currentStatus);
        }
        orderItemRepository.delete(orderItem);
    }
}
