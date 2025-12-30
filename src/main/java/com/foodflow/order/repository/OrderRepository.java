package com.foodflow.order.repository;

import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from Order o where o.id = :orderId")
    Optional<Order> findByIdForUpdate(Long orderId);

    long countByUser(User user);


    @Query("""
       select count(o)
       from Order o
       where o.deliveryPartner.id = :partnerId
       and o.status in ('OUT_FOR_PICKUP','PICKED_UP')
    """)
    long countByDeliveryPartnerActiveOrder(Long partnerId);

    long countByUserAndStatus(User user, OrderStatus status);

    @Query("""
       select count(o)
       from Order o
       where o.user.id = :userId
       and o.status not in ('DELIVERED','CANCELLED')
    """)
    long countActiveOrders(Long userId);

    List<Order> findByDeliveryPartnerIdAndStatusIn(Long partnerId, List<OrderStatus> outForPickup);
}
