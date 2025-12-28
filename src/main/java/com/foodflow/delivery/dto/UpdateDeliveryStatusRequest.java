package com.foodflow.delivery.dto;

import com.foodflow.order.enums.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateDeliveryStatusRequest {
    private OrderStatus status;
}
