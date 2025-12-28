package com.foodflow.order.service;

import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.OrderStatus;

public interface OrderCommandService {

    Order lockAndGetForAssignment(Long orderId);

    void assignDeliveryPartner(Order order, DeliveryPartner partner);

    void updateDeliveryStatus(Long partnerId, Long orderId, OrderStatus status);

    void cancelOrder(Long id, Long orderId);

    void updateOrderStatusAfterPayment(Long orderId);
}
