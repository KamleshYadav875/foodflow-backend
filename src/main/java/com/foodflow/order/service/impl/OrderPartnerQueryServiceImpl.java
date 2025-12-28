package com.foodflow.order.service.impl;

import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.repository.OrderRepository;
import com.foodflow.order.service.OrderPartnerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderPartnerQueryServiceImpl implements OrderPartnerQueryService {

    private final OrderRepository orderRepository;
    @Override
    public List<Order> getActiveOrdersForPartner(Long partnerId) {
        return orderRepository.findByDeliveryPartnerIdAndStatusIn(
                partnerId,
                List.of(
                        OrderStatus.OUT_FOR_PICKUP,
                        OrderStatus.PICKED_UP
                )
        );
    }

    @Override
    public List<Order> getCompletedOrdersForPartner(Long partnerId) {
        return orderRepository.findByDeliveryPartnerIdAndStatusIn(
                partnerId,
                List.of(
                        OrderStatus.DELIVERED,
                        OrderStatus.CANCELLED
                )
        );
    }
}
