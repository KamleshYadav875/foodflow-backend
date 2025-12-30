package com.foodflow.order.dto;

import com.foodflow.order.enums.CancelReason;
import com.foodflow.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class UserOrderResponseDto {
    private Long orderId;
    private String restaurantName;
    private Long restaurantId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime cancelledAt;
    private CancelReason cancelReason;
    private LocalDateTime deliveredAt;
    private String deliveryPartnerName;
    private Long deliveryPartnerId;
}
