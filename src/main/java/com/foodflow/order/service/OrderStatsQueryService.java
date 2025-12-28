package com.foodflow.order.service;

public interface OrderStatsQueryService {
    long countTotalOrders(Long userId);

    long countCancelledOrders(Long userId);

    long countActiveOrders(Long userId);

    long countByDeliveryPartnerActiveOrder(Long partnerId);
    long countByDeliveryPartner(Long partnerId);
}
