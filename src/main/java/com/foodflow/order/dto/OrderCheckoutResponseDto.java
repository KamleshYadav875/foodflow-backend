package com.foodflow.order.dto;

import com.foodflow.order.enums.CancelReason;
import com.foodflow.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderCheckoutResponseDto {
    private Long orderId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private String paymentLink;
    private String paymentId;
}
