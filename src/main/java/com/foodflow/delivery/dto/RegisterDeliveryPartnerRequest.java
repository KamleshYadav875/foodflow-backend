package com.foodflow.delivery.dto;

import com.foodflow.delivery.enums.VehicleType;
import lombok.Data;

@Data
public class RegisterDeliveryPartnerRequest {
    private String city;
    private VehicleType vehicleType;
}
