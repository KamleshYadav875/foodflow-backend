package com.foodflow.order.dto;

import com.foodflow.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderUpdateResponseDto {
    private Long orderId;
    private OrderStatus status;
}
