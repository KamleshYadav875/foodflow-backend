package com.foodflow.delivery.repository;

import com.foodflow.delivery.entity.OrderDeliveryAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderDeliveryAssignmentRepository extends JpaRepository<OrderDeliveryAssignment, Long> {
    Optional<OrderDeliveryAssignment> findByOrderIdAndDeliveryPartnerId(Long orderId, Long partnerId);

    @Modifying
    @Query("""
        update OrderDeliveryAssignment a
        set a.status = 'REJECTED'
        where a.orderId = :orderId
        and a.deliveryPartner.id <> :partnerId
        and a.status = 'PENDING'
    """)
    void rejectOtherAssignments(Long orderId, Long partnerId);
}
