package com.fooddelivery.orders.controller;

import com.fooddelivery.orders.dto.OrderRequestDto;
import com.fooddelivery.orders.dto.OrderResponseDto;
import com.fooddelivery.orders.entity.OrderStatus;
import com.fooddelivery.orders.security.UserPrincipal;
import com.fooddelivery.orders.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order API", description = "API for managing orders made by users")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get order by it's id",
            description = "Returns order by it's id",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable("orderId") Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @Operation(summary = "Get order by it's id with payment info",
            description = "Returns order by it's id with payment info",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order found")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{orderId}/with-payment")
    public ResponseEntity<OrderResponseDto> getOrderByIdWithPayment(@PathVariable("orderId") Long orderId) {
        OrderResponseDto orderResponseDto = orderService.getOrderByIdWithPayment(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @Operation(summary = "Get all orders by user id and order status",
            description = "Returns all orders by user id and order status",
            parameters = {@Parameter(name = "userId", description = "User's unique identifier", example = "1"),
                    @Parameter(name = "status", description = "Order status", example = "DELIVERED", required = false),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByUserId(@PathVariable("userId") Long userId,
                                                                    @RequestParam(required = false) OrderStatus status,
                                                                    Pageable pageable) {

        Page<OrderResponseDto> orders;
        if (status != null) {
            orders = orderService.getOrdersByUserIdAndStatus(userId, status, pageable);
        } else {
            orders = orderService.getOrdersByUserId(userId, pageable);
        }
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get all orders made by the current user with status filter",
            description = "Returns all orders made by the current user with order filter",
            parameters = {@Parameter(name = "status", description = "Order status", example = "PLACED", required = false),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/me")
    public ResponseEntity<Page<OrderResponseDto>> getMyOrders(Authentication authentication,
                                                              @RequestParam(required = false) OrderStatus status,
                                                              Pageable pageable) {
        UserPrincipal userPrincipal=(UserPrincipal)authentication.getPrincipal();
        Long userId=userPrincipal.getId();
        Page<OrderResponseDto> orders;
        if (status != null) {
            orders = orderService.getOrdersByUserIdAndStatus(userId, status, pageable);
        } else {
            orders = orderService.getOrdersByUserId(userId, pageable);
        }
        return ResponseEntity.ok(orders);
    }

    @Operation(summary = "Get all orders by restaurant id",
            description = "Returns all orders by restaurant id",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByRestaurantId(@PathVariable("restaurantId") Long restaurantId,
                                                                          @PageableDefault(size = 10,
                                                                                  sort = "orderDate",
                                                                                  direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDto> orders = orderService.getOrdersByRestaurantId(restaurantId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get all orders by restaurant id with active statuses",
            description = "Returns all orders by restaurant id with active statuses",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurant/{restaurantId}/active")
    public ResponseEntity<Page<OrderResponseDto>> getActiveOrdersByRestaurantId(@PathVariable("restaurantId") Long restaurantId,
                                                                                @PageableDefault(size = 10,
                                                                                        sort = "orderDate",
                                                                                        direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDto> orders = orderService.getActiveOrdersByRestaurantId(restaurantId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get all orders by restaurant id in date range",
            description = "Returns all orders by restaurant id in date range",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "startDate", description = "The start date for search", example = "2025-10-02:10-00-00"),
                    @Parameter(name = "endDate", description = "The end date for search", example = "2025-10-05:10-00-00"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurant/{restaurantId}/dateRange")
    public ResponseEntity<Page<OrderResponseDto>> getOrdersByRestaurantIdAndDateRange
            (@PathVariable("restaurantId") Long restaurantId,
             @RequestParam(required = true) LocalDateTime startDate,
             @RequestParam(required = true) LocalDateTime endDate,
             @PageableDefault(size = 10,
                     sort = "orderDate",
                     direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDto> orders = orderService.getOrdersByRestaurantIdAndDateRange(restaurantId, startDate, endDate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }

    @Operation(summary = "Get all orders with dynamic filter",
            description = "Returns all orders with dynamic filter",
            parameters = {@Parameter(name = "userId", description = "User's unique identifier", example = "1"),
                    @Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "status", description = "Order status", example = "PLACED"),
                    @Parameter(name = "afterDate", description = "The date used to filter orders created after this date",
                            example = "2025-10-02:10-00-00"),
                    @Parameter(name = "beforeDate", description = "The date used to filter orders created before this date",
                            example = "2025-10-05:10-00-00"),
                    @Parameter(name = "minPrice", description = "The min amount of sum to sort orders",
                            example = "25.90"),
                    @Parameter(name = "maxPrice", description = "The max amount of sum to sort orders",
                            example = "45.90"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of items per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Orders found")
    @ApiResponse(responseCode = "404", description = "Orders not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<Page<OrderResponseDto>> searchOrders
            (@RequestParam(required = false) Long userId,
             @RequestParam(required = false) Long restaurantId,
             @RequestParam(required = false) OrderStatus status,
             @RequestParam(required = false) LocalDateTime afterDate,
             @RequestParam(required = false) LocalDateTime beforeDate,
             @RequestParam(required = false) BigDecimal minPrice,
             @RequestParam(required = false) BigDecimal maxPrice,
             @PageableDefault(size = 10,
                     sort = "orderDate",
                     direction = Sort.Direction.DESC) Pageable pageable) {
        Page<OrderResponseDto> orders = orderService.searchOrdersWithDynamicFilters(
                userId, restaurantId, status, afterDate, beforeDate, minPrice, maxPrice, pageable
        );
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }


    @Operation(summary = "Create order for the user",
            description = "Creates order for the user with payment method",
            parameters = {@Parameter(name = "userId", description = "User's unique identifier", example = "1"),
                    @Parameter(name = "paymentMethod", description = "User's payment method", example = "CREDIT_CARD")})
    @ApiResponse(responseCode = "201", description = "Order created")
    @ApiResponse(responseCode = "400", description = "Entities mismatch")
    @ApiResponse(responseCode = "404", description = "Resource not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @ApiResponse(responseCode = "503", description = "Service unavailable")
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(Authentication authentication,
                                                       @RequestBody @Valid OrderRequestDto orderRequestDto,
                                                       @RequestParam(required = true,
                                                               defaultValue = "CREDIT_CARD") String paymentMethod) {
        UserPrincipal userPrincipal=(UserPrincipal) authentication.getPrincipal();
        Long userId=userPrincipal.getId();
        OrderResponseDto orderResponseDto = orderService.placeOrder(userId, orderRequestDto, paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @Operation(summary = "Update status for existing order",
            description = "Updates order status if available for existing order",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1"),
                    @Parameter(name = "status", description = "Order new status", example = "DELIVERED")})
    @ApiResponse(responseCode = "200", description = "Order status updated")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "409", description = "Order status conflict")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable("orderId") Long orderId,
                                                              @RequestParam(required = true) OrderStatus status) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrder);
    }

    @Operation(summary = "Delete User order",
            description = "Deletes User order if available",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "204", description = "Order was successfully deleted")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteUserOrder(@PathVariable("orderId") Long orderId) {
        orderService.deleteUserOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
