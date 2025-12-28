package com.foodflow.order.service;

import com.foodflow.order.entity.Order;

public interface OrderQueryService {

    Order getOrderById(Long orderId);
}
