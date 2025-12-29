package com.fooddelivery.orders.repository;

import com.fooddelivery.orders.entity.Payment;
import com.fooddelivery.orders.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByOrderId(Long orderId);

    @Query(value = "SELECT p FROM Payment p WHERE p.order.userId=:userId")
    Page<Payment> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT p FROM Payment p WHERE p.order.restaurantId=:restaurantId")
    Page<Payment> findByRestaurantId(@Param("restaurantId") Long restaurantId,Pageable pageable);

    Page<Payment> findByStatus(PaymentStatus status,Pageable pageable);

    @Query(value = "SELECT p FROM Payment p WHERE p.order.orderDate BETWEEN :startDate AND :endDate")
    Page<Payment> findByDateRange(@Param("startDate")LocalDateTime startDate,
                                  @Param("endDate")LocalDateTime endDate,
                                  Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(p.amount),0) FROM Payment p " +
            "WHERE p.status=com.fooddelivery.orders.entity.PaymentStatus.COMPLETED")
    BigDecimal countTotalWithCompletedStatus();
}
