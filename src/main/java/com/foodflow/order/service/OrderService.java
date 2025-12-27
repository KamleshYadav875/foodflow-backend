package com.foodflow.order.service;

import com.foodflow.order.dto.OrderResponseDto;
import com.foodflow.order.enums.OrderStatus;
import org.jspecify.annotations.Nullable;

public interface OrderService {

    OrderResponseDto checkout(Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);
}
