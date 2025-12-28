package com.foodflow.delivery.dto;

import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateAvailabilityRequest {
    @NotNull
    private DeliveryPartnerAvailability availability;
}
