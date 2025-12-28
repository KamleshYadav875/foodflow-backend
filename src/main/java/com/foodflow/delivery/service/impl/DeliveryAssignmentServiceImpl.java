package com.foodflow.delivery.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.delivery.entity.OrderDeliveryAssignment;
import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery.enums.DeliveryAssignmentStatus;
import com.foodflow.delivery.repository.DeliveryPartnerRepository;
import com.foodflow.delivery.repository.OrderDeliveryAssignmentRepository;
import com.foodflow.delivery.service.DeliveryAssignmentService;
import com.foodflow.order.entity.Order;
import com.foodflow.order.service.OrderCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeliveryAssignmentServiceImpl implements DeliveryAssignmentService {

    private final OrderCommandService orderCommandService;
    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final OrderDeliveryAssignmentRepository assignmentRepository;
    @Override
    @Transactional
    public void acceptOrder(Long orderId, Long partnerId) {

        Order order = orderCommandService.lockAndGetForAssignment(orderId);

        if (order.getDeliveryPartner() != null) {
            throw new BadRequestException("Order already assigned");
        }

        // 3️⃣ Validate assignment exists
        OrderDeliveryAssignment assignment =
                assignmentRepository
                        .findByOrderIdAndDeliveryPartnerId(orderId, partnerId)
                        .orElseThrow(() -> new BadRequestException("Offer expired"));

        if (assignment.getStatus() != DeliveryAssignmentStatus.PENDING) {
            throw new BadRequestException("Offer already processed");
        }

        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));


        orderCommandService.assignDeliveryPartner(order, partner);

        assignment.setStatus(DeliveryAssignmentStatus.ACCEPTED);
        assignmentRepository.rejectOtherAssignments(orderId, partnerId);

        partner.setAvailability(DeliveryPartnerAvailability.BUSY);
    }
}
