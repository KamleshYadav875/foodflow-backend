package com.foodflow.delivery.dto;

import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery.enums.VehicleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryPartnerResponseDto {
    private Long deliveryPartnerId;
    private Long userId;
    private String name;
    private String city;
    private VehicleType vehicleType;
    private DeliveryPartnerAvailability availability;
}
