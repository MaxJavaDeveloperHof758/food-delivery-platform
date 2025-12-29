package com.fooddelivery.orders.controller;

import com.fooddelivery.orders.dto.OrderItemResponseDto;
import com.fooddelivery.orders.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Operation(summary = "Get order items by order id",
            description = "Returns order items by order id",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order items found")
    @ApiResponse(responseCode = "404", description = "Order items not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByOrderId(@PathVariable("orderId") Long orderId){
        List<OrderItemResponseDto> orderItems=orderItemService.getOrderItemsByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItems);
    }

    @Operation(summary = "Get order items by order id with order",
            description = "Returns order items by order id with order",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order items found")
    @ApiResponse(responseCode = "404", description = "Order items not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/order/{orderId}/order")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByOrderIdWithOrder(@PathVariable("orderId") Long orderId){
        List<OrderItemResponseDto> orderItems=orderItemService.getOrderItemsByOrderIdWithOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItems);
    }

    @Operation(summary = "Get one order item by it's id",
            description = "Returns one order item by it's id",
            parameters = {@Parameter(name = "orderItemId", description = "Order item's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order item found")
    @ApiResponse(responseCode = "404", description = "Order item not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{orderItemId}")
    public ResponseEntity<OrderItemResponseDto> getOrderItemById(@PathVariable("orderItemId") Long orderItemId){
        OrderItemResponseDto orderItem=orderItemService.getOrderItemById(orderItemId);
        return ResponseEntity.status(HttpStatus.OK).body(orderItem);
    }

    @Operation(summary = "Get the total sold items by dish id",
            description = "Returns the total sold items by dish id",
            parameters = {@Parameter(name = "dishId", description = "Dish's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Order item found")
    @ApiResponse(responseCode = "404", description = "Order item not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/dish/{dishId}/total-sold")
    public ResponseEntity<Integer> getTotalSoldQuantityByDishId(@PathVariable("dishId") Long dishId){
        Integer totalSold=orderItemService.getTotalSoldQuantityByDishId(dishId);
        return ResponseEntity.status(HttpStatus.OK).body(totalSold);
    }

    @Operation(summary = "Update the quantity of order item",
            description = "Updates the quantity of order item",
            parameters = {@Parameter(name = "orderItemId", description = "Order item unique identifier", example = "1"),
            @Parameter(name = "newQuantity",description = "New value for order item quantity",example = "10")})
    @ApiResponse(responseCode = "200", description = "Quantity of order item was successfully updated")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "404", description = "Order item not found")
    @ApiResponse(responseCode = "409", description = "Modification exception")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PatchMapping("/{orderItemId}/quantity")
    public ResponseEntity<OrderItemResponseDto> updateOrderItemQuantity(@PathVariable("orderItemId") Long orderItemId,
                                                                        @RequestParam @Min(1) Integer newQuantity){
        OrderItemResponseDto updatedOrderItem= orderItemService.updateOrderItemQuantity(orderItemId,newQuantity);
        return ResponseEntity.status(HttpStatus.OK).body(updatedOrderItem);
    }

    @Operation(summary = "Delete existing order item from the order",
            description = "Deletes existing order item from the order",
            parameters = {@Parameter(name = "orderItemId", description = "Order item unique identifier", example = "1")})
    @ApiResponse(responseCode = "204", description = "Order item was successfully deleted")
    @ApiResponse(responseCode = "404", description = "Order item not found")
    @ApiResponse(responseCode = "409", description = "Modification exception")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @DeleteMapping("/{orderItemId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable("orderItemId") Long orderItemId){
        orderItemService.deleteOrderItem(orderItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
