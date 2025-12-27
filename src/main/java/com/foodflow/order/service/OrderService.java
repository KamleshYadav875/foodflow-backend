package com.foodflow.order.service;

import com.foodflow.order.dto.OrderResponseDto;
import com.foodflow.order.dto.PageResponse;
import com.foodflow.order.enums.OrderStatus;
import org.jspecify.annotations.Nullable;

public interface OrderService {

    OrderResponseDto checkout(Long userId);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);

    PageResponse<OrderResponseDto> getOrdersByUser(Long userId, int page, int size);

    PageResponse<OrderResponseDto> getOrderByRestaurant(Long restaurantId, int page, int size);
}
