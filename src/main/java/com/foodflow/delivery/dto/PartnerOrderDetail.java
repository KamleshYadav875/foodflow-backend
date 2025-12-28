package com.foodflow.delivery.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartnerOrderDetail {
    private Long orderId;
    private String restaurantName;
    private String restaurantAddress;
    private String userName;
    private String userAddress;
    private Integer totalItems;
    private String orderStatus;
    private LocalDateTime orderedAt;
    private LocalDateTime deliveredAt;
}
