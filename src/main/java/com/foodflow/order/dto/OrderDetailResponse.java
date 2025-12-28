package com.foodflow.order.dto;

import com.foodflow.order.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDetailResponse {

    private Long orderId;
    private OrderStatus status;
    private LocalDateTime orderedAt;
    private String restaurantName;
    private Long partnerId;
    private String partnerName;
    private Double partnerRating;
    private List<OrderItemResponse> items;
}
