package com.foodflow.order.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.CancelReason;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.repository.OrderRepository;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.util.OrderStatusValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCommandServiceImpl implements OrderCommandService {

    private final OrderRepository orderRepository;

    @Override
    public Order lockAndGetForAssignment(Long orderId) {
        return orderRepository.findByIdForUpdate(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ORDER_NOT_FOUND));
    }

    @Override
    public void assignDeliveryPartner(Order order, DeliveryPartner partner) {
        order.setDeliveryPartner(partner);
        order.setStatus(OrderStatus.OUT_FOR_PICKUP);
        order.setAssignedAt(LocalDateTime.now());
    }

    @Override
    public void updateDeliveryStatus(Long orderId, Long partnerId,  OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ORDER_NOT_FOUND));


        if(!order.getDeliveryPartner().getId().equals(partnerId)){
            throw new BadRequestException("Not assigned to this order");
        }

        OrderStatusValidator.validateTransition(
                order.getStatus(),
                status
        );


        if(status.equals(OrderStatus.DELIVERED)){
            order.setStatus(OrderStatus.DELIVERED);
            order.setDeliveredAt(LocalDateTime.now());
        }
        else if(status.equals(OrderStatus.PICKED_UP)){
            order.setStatus(OrderStatus.PICKED_UP);
            order.setPickedUpAt(LocalDateTime.now());
        }

        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ORDER_NOT_FOUND));

        if(!order.getUser().getId().equals(userId)){
            throw new BadRequestException("You are not allow to cancel the order");
        }

        OrderStatusValidator.validateTransition(order.getStatus(), OrderStatus.CANCELLED);

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(CancelReason.USER_REQUEST);
        orderRepository.save(order);
    }

    @Override
    public void updateOrderStatusAfterPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.ORDER_NOT_FOUND));

        order.setStatus(OrderStatus.PLACED);
        orderRepository.save(order);
    }
}
