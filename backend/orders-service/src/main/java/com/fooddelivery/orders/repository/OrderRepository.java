package com.fooddelivery.orders.repository;

import com.fooddelivery.orders.entity.Order;
import com.fooddelivery.orders.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long>, JpaSpecificationExecutor<Order> {
    @EntityGraph(attributePaths = {"items"})
    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.payment WHERE o.id=:id")
    Optional<Order> findByIdWithPayment(@Param("id") Long id);

    @EntityGraph(attributePaths = {"items"})
    Page<Order> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"items"})
    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status,Pageable pageable);

    @EntityGraph(attributePaths = {"items"})
    Page<Order> findByRestaurantId(Long restaurantId,Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.restaurantId = :restaurantId " +
            "AND o.status IN (com.fooddelivery.orders.entity.OrderStatus.PLACED, " +
            "com.fooddelivery.orders.entity.OrderStatus.PENDING, " +
            "com.fooddelivery.orders.entity.OrderStatus.IN_PROGRESS) " +
            "ORDER BY o.orderDate DESC")
    Page<Order> findActiveByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query(value = "SELECT o FROM Order o WHERE o.restaurantId=:restaurantId AND "
    +"o.orderDate BETWEEN :startDate AND :endDate")
    Page<Order> findByRestaurantIdAndDateRange(@Param("restaurantId") Long restaurantId,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);

    static Specification<Order> hasUserId(Long userId){
        return (root,cq,cb)->userId==null?null:cb.equal(root.get("userId"),userId);
    }
    static Specification<Order> hasRestaurantId(Long restaurantId){
        return (root,cq,cb)->restaurantId==null?null:cb.equal(root.get("restaurantId"),restaurantId);
    }
    static Specification<Order> hasStatus(OrderStatus status){
        return (root,query,cb)->status==null?null:cb.equal(root.get("status"),status);
    }
    static Specification<Order> createdAfter(LocalDateTime date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThanOrEqualTo(root.get("orderDate"), date);
    }
    static Specification<Order> createdBefore(LocalDateTime date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("orderDate"), date);
    }
    static Specification<Order> hasMinTotalPrice(BigDecimal minPrice) {
        return (root, query, cb) ->
                minPrice == null ? null : cb.greaterThanOrEqualTo(root.get("totalPrice"), minPrice);
    }
    static Specification<Order> hasMaxTotalPrice(BigDecimal maxPrice) {
        return (root, query, cb) ->
                maxPrice == null ? null : cb.lessThanOrEqualTo(root.get("totalPrice"), maxPrice);
    }
}
