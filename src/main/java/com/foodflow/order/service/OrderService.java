package com.foodflow.order.service;

import com.foodflow.order.dto.*;
import com.foodflow.order.enums.OrderStatus;
import org.jspecify.annotations.Nullable;

public interface OrderService {

    OrderCheckoutResponseDto checkout(Long userId);

    OrderUpdateResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

    PageResponse<UserOrderResponseDto> getOrdersByUser(Long userId, int page, int size);

    PageResponse<OrderResponseDto> getOrderByRestaurant(Long restaurantId, int page, int size);

    OrderDetailResponse getUserOrderDetails(Long userId, Long orderId);
}
