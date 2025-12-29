package com.fooddelivery.orders.repository;

import com.fooddelivery.orders.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query(value = "SELECT oi FROM OrderItem oi JOIN FETCH oi.order WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithOrder(@Param("orderId") Long orderId);

    @Query(value = "SELECT COALESCE(SUM(oi.quantity),0) FROM OrderItem oi WHERE oi.dishId=:dishId")
    Integer getTotalSoldQuantityByDishId(@Param("dishId") Long dishId);
}
