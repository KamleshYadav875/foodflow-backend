package com.foodflow.order.dto;

import com.foodflow.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class MyOrderResponseDto {

    private Long orderId;
    private OrderStatus status;

    private BigDecimal totalAmount;
    private Integer totalItems;

    private String restaurantName;

    private LocalDateTime createdAt;
}
