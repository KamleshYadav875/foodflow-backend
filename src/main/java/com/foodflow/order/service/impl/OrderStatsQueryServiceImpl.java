package com.foodflow.order.service.impl;

import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.delivery.service.DeliveryPartnerQueryService;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.repository.OrderRepository;
import com.foodflow.order.service.OrderStatsQueryService;
import com.foodflow.user.entity.User;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderStatsQueryServiceImpl implements OrderStatsQueryService {

    private final OrderRepository orderRepository;
    private final UserQueryService userQueryService;
    private final DeliveryPartnerQueryService partnerQueryService;

    @Override
    public long countTotalOrders(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        return orderRepository.countByUser(user);
    }

    @Override
    public long countCancelledOrders(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        return orderRepository.countByUserAndStatus(user, OrderStatus.CANCELLED);
    }

    @Override
    public long countActiveOrders(Long userId) {
        return orderRepository.countActiveOrders(userId);
    }

    @Override
    public long countByDeliveryPartnerActiveOrder(Long partnerId) {
        DeliveryPartner partner = partnerQueryService.getPartnerById(partnerId);
        return orderRepository.countByDeliveryPartnerActiveOrder(partner.getId());
    }

}
