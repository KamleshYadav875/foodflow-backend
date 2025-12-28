package com.foodflow.delivery.dto;

import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PartnerProfileResponseDto {

    private Long partnerId;
    private String name;
    private String city;
    private DeliveryPartnerAvailability availability;
    private VehicleType vehicleType;
    private Long totalDeliveries;
    private Long activeOrders;
    private Double rating;
    private Boolean isActive;
    private LocalDateTime joinedAt;
}
