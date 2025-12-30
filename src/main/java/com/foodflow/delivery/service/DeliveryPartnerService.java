package com.foodflow.delivery.service;


import com.foodflow.delivery.dto.DeliveryPartnerResponseDto;
import com.foodflow.delivery.dto.PartnerOrderDetail;
import com.foodflow.delivery.dto.PartnerProfileResponseDto;
import com.foodflow.delivery.dto.RegisterDeliveryPartnerRequest;
import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.order.enums.OrderStatus;

import java.util.List;

public interface DeliveryPartnerService {

    DeliveryPartnerResponseDto register(RegisterDeliveryPartnerRequest request);

    DeliveryPartnerResponseDto updateAvailability(Long partnerId, DeliveryPartnerAvailability availability);

    PartnerProfileResponseDto getPartnerProfile(Long userId);

    List<PartnerOrderDetail> getPartnerCurrentOrder(Long userId);

    List<PartnerOrderDetail> getPartnerOrderHistory(Long userId);

    void updateStatus(Long userId, Long orderId, OrderStatus status);
}
