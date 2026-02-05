package com.fooddelivery.orders.controller;

import com.fooddelivery.orders.dto.PaymentResponseDto;
import com.fooddelivery.orders.entity.PaymentStatus;
import com.fooddelivery.orders.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "API for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Get payment by order id",
            description = "Returns payment by order id",
            parameters = {@Parameter(name = "orderId", description = "Order's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponseDto> getPaymentByOrderId(@PathVariable("orderId") Long orderId) {
        PaymentResponseDto payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

    @Operation(summary = "Get payment by it's id",
            description = "Returns payment by it's id",
            parameters = {@Parameter(name = "paymentId", description = "Payment's unique identifier", example = "1")})
    @ApiResponse(responseCode = "200", description = "Payment found")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable("paymentId") Long paymentId) {
        PaymentResponseDto payment = paymentService.getPaymentById(paymentId);
        return ResponseEntity.status(HttpStatus.OK).body(payment);
    }

    @Operation(summary = "Get payments by user id",
            description = "Returns payments by user id",
            parameters = {@Parameter(name = "userId", description = "User's unique identifier", example = "1"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of payments per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Payments found")
    @ApiResponse(responseCode = "404", description = "Payments not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByUserId(@PathVariable("userId") Long userId,
                                                                        Pageable pageable) {
        Page<PaymentResponseDto> payments = paymentService.getPaymentsByUserId(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @Operation(summary = "Get payments by restaurant id",
            description = "Returns payments by restaurant id",
            parameters = {@Parameter(name = "restaurantId", description = "Restaurant's unique identifier", example = "1"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of payments per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Payments found")
    @ApiResponse(responseCode = "404", description = "Payments not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByRestaurantId(
            @PathVariable("restaurantId") Long restaurantId,
            @PageableDefault(size = 10,
                    sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PaymentResponseDto> payments = paymentService.getPaymentsByRestaurantId(restaurantId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @Operation(summary = "Get payments by payment status",
            description = "Returns payments by payment status",
            parameters = {@Parameter(name = "status", description = "Payment status", example = "COMPLETED"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of payments per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Payments found")
    @ApiResponse(responseCode = "404", description = "Payments not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByStatus(@RequestParam PaymentStatus status,
                                                                        Pageable pageable) {
        Page<PaymentResponseDto> payments = paymentService.getPaymentsByStatus(status, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @Operation(summary = "Get payments by date range",
            description = "Returns payments by date range",
            parameters = {@Parameter(name = "startDate",
                    description = "The date from which we search payments", example = "2025-10-02:10-00-00"),
                    @Parameter(name = "endDate",
                            description = "The date up to which we search payments", example = "2025-10-05:10-30-00"),
                    @Parameter(name = "page", description = "Page number (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Number of payments per page", example = "10")})
    @ApiResponse(responseCode = "200", description = "Payments found")
    @ApiResponse(responseCode = "404", description = "Payments not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dateRange")
    public ResponseEntity<Page<PaymentResponseDto>> getPaymentsByDateRange(@RequestParam LocalDateTime startDate,
                                                                           @RequestParam LocalDateTime endDate,
                                                                           @PageableDefault(size = 10,
                                                                                   sort = "id",
                                                                                   direction = Sort.Direction.ASC) Pageable pageable) {
        Page<PaymentResponseDto> payments = paymentService.getPaymentsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(payments);
    }

    @Operation(summary = "Get the total sum with completed payments",
            description = "Returns the total sum with completed payments")
    @ApiResponse(responseCode = "200", description = "The total sum is received")
    @ApiResponse(responseCode = "404", description = "Payments not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/total/completed")
    public ResponseEntity<BigDecimal> getTotalCompletedAmount() {
        BigDecimal totalSum = paymentService.countTotalWithCompletedStatus();
        return ResponseEntity.status(HttpStatus.OK).body(totalSum);
    }

    @Operation(summary = "Create payment for existing order",
            description = "Creates payment for existing order",
            parameters = {@Parameter(name = "orderId", description = "Order unique identifier", example = "1"),
                    @Parameter(name = "paymentMethod",
                            description = "Payment method",example = "CREDIT_CARD (used by default)")})
    @ApiResponse(responseCode = "201", description = "Payment was successfully created")
    @ApiResponse(responseCode = "404", description = "Order not found")
    @ApiResponse(responseCode = "409", description = "Payment already exists")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/orders/{orderId}")
    public ResponseEntity<PaymentResponseDto> createPaymentForOrder(@PathVariable("orderId") Long orderId,
                                                                    @RequestParam(defaultValue = "CREDIT_CARD") String paymentMethod) {
        PaymentResponseDto createdPayment = paymentService.createPaymentForOrder(orderId, paymentMethod);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @Operation(summary = "Update payment status",
            description = "Updates payment status",
            parameters = {@Parameter(name = "paymentId", description = "Payment unique identifier", example = "1"),
                    @Parameter(name = "newStatus",
                            description = "New status for the payment",example = "COMPLETED")})
    @ApiResponse(responseCode = "200", description = "Payment status was successfully updated")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{paymentId}/status")
    public ResponseEntity<PaymentResponseDto> updatePaymentStatus(@PathVariable("paymentId") Long paymentId,
                                                                  @RequestParam PaymentStatus newStatus) {
        PaymentResponseDto updatedPayment = paymentService.updatePaymentStatus(paymentId, newStatus);
        return ResponseEntity.status(HttpStatus.OK).body(updatedPayment);
    }

    @Operation(summary = "Refund payment",
            description = "Refunds payment",
            parameters = {@Parameter(name = "paymentId", description = "Payment unique identifier", example = "1"),
                    @Parameter(name = "refundAmount",
                            description = "The sum which will be refunded",example = "20.90")})
    @ApiResponse(responseCode = "200", description = "Payment was successfully refunded")
    @ApiResponse(responseCode = "400", description = "Payment refund failure")
    @ApiResponse(responseCode = "404", description = "Payment not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponseDto> refundPayment(@PathVariable("paymentId") Long paymentId,
                                                            @RequestParam BigDecimal refundAmount) {
        PaymentResponseDto refundedPayment = paymentService.refundPayment(paymentId, refundAmount);
        return ResponseEntity.status(HttpStatus.OK).body(refundedPayment);
    }
}
