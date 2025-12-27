package com.foodflow.order.repository;

import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o FROM Order o
        WHERE o.status = :status
        AND o.createdAt < :expiryTime
        """)
    List<Order> findExpiredOrders(OrderStatus status, LocalDateTime expiryTime);

    Page<Order> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<Order> findByRestaurantOrderByCreatedAtDesc(Restaurant restaurant, Pageable pageable);
}
