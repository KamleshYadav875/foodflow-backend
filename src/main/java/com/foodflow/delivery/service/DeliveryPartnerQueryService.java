package com.foodflow.delivery.service;

import com.foodflow.delivery.entity.DeliveryPartner;

public interface DeliveryPartnerQueryService {

    DeliveryPartner getPartnerById(Long partnerId);
}
