package com.foodflow.order.service;

import com.foodflow.order.entity.Order;

import java.util.List;

public interface OrderPartnerQueryService {

    List<Order> getActiveOrdersForPartner(Long partnerId);

    List<Order> getCompletedOrdersForPartner(Long partnerId);
}
