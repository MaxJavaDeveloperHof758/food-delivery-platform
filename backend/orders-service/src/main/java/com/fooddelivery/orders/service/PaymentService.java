package com.fooddelivery.orders.service;

import com.fooddelivery.orders.dto.PaymentResponseDto;
import com.fooddelivery.orders.entity.Order;
import com.fooddelivery.orders.entity.Payment;
import com.fooddelivery.orders.entity.PaymentStatus;
import com.fooddelivery.orders.exception.RefundPaymentException;
import com.fooddelivery.orders.exception.ResourceAlreadyExistsException;
import com.fooddelivery.orders.exception.ResourceNotFoundException;
import com.fooddelivery.orders.mapper.PaymentMapper;
import com.fooddelivery.orders.repository.OrderRepository;
import com.fooddelivery.orders.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDto createPaymentForOrder(Long orderId,String paymentMethod){
        log.info("Creating payment for order: {}",orderId);
        Order order=orderRepository.findById(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Order not found with id "+orderId));

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new ResourceAlreadyExistsException("Payment already exists for order: " + orderId);
        }

        String method = paymentMethod != null ? paymentMethod : "CREDIT_CARD";
        Payment payment = Payment.builder().order(order).method(method)
                .amount(order.getTotalPrice()).status(PaymentStatus.PAID).build();
        Payment savedPayment=paymentRepository.save(payment);
        log.info("Payment {} created for order {}", savedPayment.getId(), order.getId());
        return paymentMapper.paymentToPaymentResponseDto(savedPayment);
    }
    public PaymentResponseDto getPaymentByOrderId(Long orderId){
        log.info("Getting payment for order: {}", orderId);
        Payment payment=paymentRepository.findByOrderId(orderId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found for order "+orderId));
        return paymentMapper.paymentToPaymentResponseDto(payment);
    }
    public PaymentResponseDto getPaymentById(Long paymentId){
        log.info("Getting payment: {}",paymentId);
        Payment payment=paymentRepository.findById(paymentId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+paymentId));
        return paymentMapper.paymentToPaymentResponseDto(payment);
    }
    public Page<PaymentResponseDto> getPaymentsByUserId(Long userId, Pageable pageable){
        log.info("Getting payments for user: {}",userId);
        Page<Payment> payments=paymentRepository.findByUserId(userId,pageable);
        return paymentMapper.pagePaymentToPagePaymentDto(payments);
    }
    public Page<PaymentResponseDto> getPaymentsByRestaurantId(Long restaurantId, Pageable pageable){
        log.info("Getting payments for restaurant: {}",restaurantId);
        Page<Payment> payments=paymentRepository.findByRestaurantId(restaurantId,pageable);
        return paymentMapper.pagePaymentToPagePaymentDto(payments);
    }
    public Page<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status, Pageable pageable){
        log.info("Getting payments with status: {}",status);
        Page<Payment> payments=paymentRepository.findByStatus(status,pageable);
        return paymentMapper.pagePaymentToPagePaymentDto(payments);
    }
    public Page<PaymentResponseDto> getPaymentsByDateRange(LocalDateTime startDate,LocalDateTime endDate, Pageable pageable){
        log.info("Getting payments from: {} to {}",startDate,endDate);
        Page<Payment> payments=paymentRepository.findByDateRange(startDate,endDate,pageable);
        return paymentMapper.pagePaymentToPagePaymentDto(payments);
    }
    public BigDecimal countTotalWithCompletedStatus(){
        log.info("Getting total completed payments amount");
        return paymentRepository.countTotalWithCompletedStatus();
    }
    @Transactional
    public PaymentResponseDto updatePaymentStatus(Long paymentId,PaymentStatus newStatus){
        log.info("Updating payment {} status to {}", paymentId, newStatus);
        Payment payment=paymentRepository.findById(paymentId)
                .orElseThrow(()->new ResourceNotFoundException("Payment not found with id "+paymentId));
        payment.setStatus(newStatus);
        Payment updatedPayment=paymentRepository.save(payment);
        return paymentMapper.paymentToPaymentResponseDto(updatedPayment);
    }
    @Transactional
    public PaymentResponseDto refundPayment(Long paymentId,BigDecimal refundAmount) {
        log.info("Refunding payment: {}, amount: {}", paymentId, refundAmount);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id " + paymentId));
        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new RefundPaymentException("Cannot refund - payment status is " + payment.getStatus());
        }
        if (refundAmount.compareTo(payment.getAmount()) > 0) {
            throw new RefundPaymentException(
                    "Refund amount cannot exceed payment amount: " + payment.getAmount()
            );
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updated = paymentRepository.save(payment);
        return paymentMapper.paymentToPaymentResponseDto(updated);
    }
}
