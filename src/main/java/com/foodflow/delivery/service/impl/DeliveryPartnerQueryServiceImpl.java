package com.foodflow.delivery.service.impl;

import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.delivery.repository.DeliveryPartnerRepository;
import com.foodflow.delivery.service.DeliveryPartnerQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerQueryServiceImpl implements DeliveryPartnerQueryService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    @Override
    public DeliveryPartner getPartnerById(Long partnerId) {

        return deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found"));
    }
}
