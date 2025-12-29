package com.fooddelivery.orders.service;

import com.fooddelivery.orders.dto.OrderItemRequestDto;
import com.fooddelivery.orders.dto.OrderRequestDto;
import com.fooddelivery.orders.dto.OrderResponseDto;
import com.fooddelivery.orders.entity.*;
import com.fooddelivery.orders.exception.*;
import com.fooddelivery.orders.integration.RestaurantServiceClient;
import com.fooddelivery.orders.integration.UserServiceClient;
import com.fooddelivery.orders.integration.dto.AddressResponseDto;
import com.fooddelivery.orders.integration.dto.DishResponseDto;
import com.fooddelivery.orders.mapper.OrderMapper;
import com.fooddelivery.orders.mapper.PaymentMapper;
import com.fooddelivery.orders.repository.OrderRepository;
import com.fooddelivery.orders.repository.PaymentRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserServiceClient userServiceClient;
    private final RestaurantServiceClient restaurantServiceClient;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService paymentService;
    private final OrderMapper orderMapper;
    private final PaymentMapper paymentMapper;

    @Transactional
    public OrderResponseDto placeOrder(Long userId, OrderRequestDto orderRequestDto, String paymentMethod) {
        log.info("Creating order for user: {}, restaurant: {}", userId, orderRequestDto.getRestaurantId());
        Boolean checkUser, checkRestaurant;
        try {
            checkUser = userServiceClient.checkUserExists(userId);
        } catch (FeignException e) {
            log.error("Failed to find user from users-service", e);
            throw new ServiceUnavailableException("User service unavailable");
        }
        if (!checkUser) {
            throw new ResourceNotFoundException("User not found with id " + userId);
        }
        try {
            checkRestaurant = restaurantServiceClient.checkRestaurantExists(orderRequestDto.getRestaurantId());
        } catch (FeignException ex1) {
            log.error("Failed to find restaurant from restaurants-service", ex1);
            throw new ServiceUnavailableException("Restaurants-service unavailable");
        }
        if (!checkRestaurant) {
            throw new ResourceNotFoundException("Restaurant not found with id " + orderRequestDto.getRestaurantId());
        }
        List<AddressResponseDto> userAddresses;
        try {
            userAddresses = userServiceClient.getAddressesByUserId(userId);
        } catch (FeignException ex2) {
            log.error("Failed to fetch addresses from users-service", ex2);
            throw new ServiceUnavailableException("Users-service unavailable");
        }
        boolean isValid = userAddresses.stream()
                .anyMatch(address -> address.getId().equals(orderRequestDto.getDeliveryAddressId()));
        if (!isValid) {
            throw new AddressNotBelongsToUserException("Address does not belong to user or not found");
        }
        Order order = Order.builder().userId(userId).restaurantId(orderRequestDto.getRestaurantId())
                .deliveryAddressId(orderRequestDto.getDeliveryAddressId()).status(OrderStatus.PLACED).build();
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.valueOf(0.00);
        DishResponseDto dish;
        for (OrderItemRequestDto item : orderRequestDto.getItems()) {
            try {
                dish = restaurantServiceClient.getDishByRestaurantAndDishId(
                        orderRequestDto.getRestaurantId(), item.getDishId());
                if (!dish.getRestaurantId().equals(orderRequestDto.getRestaurantId())) {
                    throw new DishNotBelongsToRestaurantException("Dish doesn't belong to restaurant");
                }
            } catch (FeignException ex3) {
                log.error("Failed to fetch dish from restaurants-service", ex3);
                throw new ServiceUnavailableException("Restaurants-service unavailable");
            }
            OrderItem orderItem = OrderItem.builder().order(order).dishId(dish.getId())
                    .quantity(item.getQuantity()).price(dish.getPrice()).build();
            orderItems.add(orderItem);
            totalPrice = totalPrice.add(
                    dish.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            );
        }
        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        Payment payment = createPaymentDirectly(savedOrder, paymentMethod);
        savedOrder.setPayment(payment);
        log.info("Order {} created for user {}", savedOrder.getId(), userId);
        return orderMapper.orderToOrderResponseDto(savedOrder);
    }

    private Payment createPaymentDirectly(Order order, String paymentMethod) {
        String method = paymentMethod != null ? paymentMethod : "CREDIT_CARD";

        Payment payment = Payment.builder()
                .order(order)
                .method(method)
                .amount(order.getTotalPrice())
                .status(PaymentStatus.PAID)
                .build();

        return paymentRepository.save(payment);
    }

    public OrderResponseDto getOrderById(Long id) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return orderMapper.orderToOrderResponseDto(existingOrder);
    }

    public OrderResponseDto getOrderByIdWithPayment(Long id) {
        Order existingOrder = orderRepository.findByIdWithPayment(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return orderMapper.orderToOrderResponseDto(existingOrder);
    }

    public Page<OrderResponseDto> getOrdersByUserId(Long userId, Pageable pageable) {
        return orderMapper.pageOrdersToPageOrdersDto(orderRepository.findByUserId(userId, pageable));
    }

    public Page<OrderResponseDto> getOrdersByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable) {
        return orderMapper.pageOrdersToPageOrdersDto(orderRepository.findByUserIdAndStatus(userId, status, pageable));
    }

    public Page<OrderResponseDto> getOrdersByRestaurantId(Long restaurantId, Pageable pageable) {
        return orderMapper.pageOrdersToPageOrdersDto(orderRepository.findByRestaurantId(restaurantId, pageable));
    }

    public Page<OrderResponseDto> getActiveOrdersByRestaurantId(Long restaurantId, Pageable pageable) {
        return orderMapper.pageOrdersToPageOrdersDto(orderRepository.findActiveByRestaurantId(restaurantId, pageable));
    }

    public Page<OrderResponseDto> getOrdersByRestaurantIdAndDateRange(Long restaurantId,
                                                                      LocalDateTime startDate,
                                                                      LocalDateTime endDate,
                                                                      Pageable pageable) {
        return orderMapper.pageOrdersToPageOrdersDto(orderRepository
                .findByRestaurantIdAndDateRange(restaurantId, startDate, endDate, pageable));
    }

    public Page<OrderResponseDto> searchOrdersWithDynamicFilters(Long userId, Long restaurantId,
                                                                 OrderStatus status, LocalDateTime afterDate,
                                                                 LocalDateTime beforeDate, BigDecimal minPrice,
                                                                 BigDecimal maxPrice, Pageable pageable) {
        List<Specification<Order>> specifications = new ArrayList<>();
        if (userId != null) specifications.add(OrderRepository.hasUserId(userId));
        if (restaurantId != null) specifications.add(OrderRepository.hasRestaurantId(restaurantId));
        if (status != null) specifications.add(OrderRepository.hasStatus(status));
        if (afterDate != null) specifications.add(OrderRepository.createdAfter(afterDate));
        if (beforeDate != null) specifications.add(OrderRepository.createdBefore(beforeDate));
        if (minPrice != null) specifications.add(OrderRepository.hasMinTotalPrice(minPrice));
        if (maxPrice != null) specifications.add(OrderRepository.hasMaxTotalPrice(maxPrice));
        Specification<Order> spec = specifications.isEmpty()
                ? null
                : Specification.allOf(specifications);

        assert spec != null;
        return orderRepository.findAll(spec, pageable)
                .map(orderMapper::orderToOrderResponseDto);
    }

    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus newStatus) {
        log.info("Updating order {} status to {}", orderId, newStatus);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        if (order.getStatus().equals(OrderStatus.COMPLETED) ||
                order.getStatus().equals(OrderStatus.CANCELED)) {
            throw new UpdateStatusException(
                    "Cannot update status of completed or canceled order");
        }
        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        order.setStatusUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        log.info("Order {} status updated to {} at {}",
                orderId, newStatus, order.getStatusUpdatedAt());
        return orderMapper.orderToOrderResponseDto(order);
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus newStatus) {
        if (current.equals(newStatus)) {
            throw new UpdateStatusException("Order is already in status: " + current);
        }
        if (current.equals(OrderStatus.COMPLETED) || current.equals(OrderStatus.CANCELED)) {
            throw new UpdateStatusException(
                    String.format("Cannot change status from %s to %s. Order is already finalized.",
                            current, newStatus)
            );
        }
    }

    public void deleteUserOrder(Long orderId) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));
        orderRepository.deleteById(orderId);
    }
}
