package com.foodflow.delivery.service;

public interface DeliveryAssignmentService {

    void acceptOrder(Long orderId, Long partnerId);
}
