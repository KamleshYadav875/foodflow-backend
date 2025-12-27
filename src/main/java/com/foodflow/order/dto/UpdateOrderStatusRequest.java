package com.foodflow.order.dto;

import com.foodflow.order.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
