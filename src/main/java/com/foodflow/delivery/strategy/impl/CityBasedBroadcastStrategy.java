package com.foodflow.delivery.strategy.impl;

import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.delivery.entity.OrderDeliveryAssignment;
import com.foodflow.delivery.enums.DeliveryAssignmentStatus;
import com.foodflow.delivery.repository.DeliveryPartnerRepository;
import com.foodflow.delivery.repository.OrderDeliveryAssignmentRepository;
import com.foodflow.delivery.strategy.DeliveryAssignmentStrategy;
import com.foodflow.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityBasedBroadcastStrategy implements DeliveryAssignmentStrategy {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderDeliveryAssignmentRepository deliveryAssignmentRepository;
    @Override
    public void broadcast(Order order) {

        List<DeliveryPartner> partners = deliveryPartnerRepository.findOnlineByCity(order.getRestaurant().getCity());

        for (DeliveryPartner partner : partners) {

            OrderDeliveryAssignment assignment =
                    OrderDeliveryAssignment.builder()
                            .orderId(order.getId())
                            .deliveryPartner(partner)
                            .status(DeliveryAssignmentStatus.PENDING)
                            .build();

            deliveryAssignmentRepository.save(assignment);

            log.info("Broadcast order {} to partner {}", order.getId(), partner.getId());
        }
    }
}
