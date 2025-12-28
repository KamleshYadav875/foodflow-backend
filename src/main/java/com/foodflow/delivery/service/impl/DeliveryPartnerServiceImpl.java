package com.foodflow.delivery.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.delivery.dto.DeliveryPartnerResponseDto;
import com.foodflow.delivery.dto.PartnerOrderDetail;
import com.foodflow.delivery.dto.PartnerProfileResponseDto;
import com.foodflow.delivery.dto.RegisterDeliveryPartnerRequest;
import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery.repository.DeliveryPartnerRepository;
import com.foodflow.delivery.service.DeliveryPartnerService;
import com.foodflow.order.entity.Order;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.service.OrderCommandService;
import com.foodflow.order.service.OrderPartnerQueryService;
import com.foodflow.order.service.OrderService;
import com.foodflow.order.service.OrderStatsQueryService;
import com.foodflow.user.entity.User;
import com.foodflow.user.enums.UserRole;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPartnerServiceImpl implements DeliveryPartnerService {

    private final DeliveryPartnerRepository deliveryPartnerRepository;
    private final UserQueryService userQueryService;
    private final OrderStatsQueryService orderStatsQueryService;
    private final OrderPartnerQueryService partnerQueryService;
    private final OrderCommandService orderCommandService;

    @Override
    public DeliveryPartnerResponseDto register(RegisterDeliveryPartnerRequest request) {
        User user = userQueryService.getUserById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        if(deliveryPartnerRepository.existsByUser(user))
        {
            throw new BadRequestException("User is already registered as delivery partner");
        }

        user.setRole(UserRole.DELIVERY);

        DeliveryPartner deliveryPartner = DeliveryPartner.builder()
                .user(user)
                .vehicleType(request.getVehicleType())
                .availability(DeliveryPartnerAvailability.OFFLINE)
                .city(request.getCity())
                .rating(0.0)
                .totalDeliveries(0)
                .isActive(true)
                .build();

        DeliveryPartner savedPartner = deliveryPartnerRepository.save(deliveryPartner);
        return DeliveryPartnerResponseDto.builder()
                .deliveryPartnerId(savedPartner.getId())
                .availability(savedPartner.getAvailability())
                .name(user.getName())
                .city(savedPartner.getCity())
                .userId(user.getId())
                .vehicleType(savedPartner.getVehicleType())
                .build();
    }

    @Override
    public DeliveryPartnerResponseDto updateAvailability(Long partnerId, DeliveryPartnerAvailability availability) {

        DeliveryPartner partner = deliveryPartnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery partner not found"));


        if(availability == DeliveryPartnerAvailability.BUSY){
            throw new BadRequestException("BUSY status is system-controlled");
        }

        partner.setAvailability(availability);
        DeliveryPartner updated = deliveryPartnerRepository.save(partner);

        return DeliveryPartnerResponseDto.builder()
                .deliveryPartnerId(updated.getId())
                .userId(updated.getUser().getId())
                .city(updated.getCity())
                .name(updated.getUser().getName())
                .vehicleType(updated.getVehicleType())
                .availability(updated.getAvailability())
                .build();
    }

    @Override
    public PartnerProfileResponseDto getPartnerProfile(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        DeliveryPartner partner = deliveryPartnerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_PARTNER));

        Long activeOrders = orderStatsQueryService.countByDeliveryPartnerActiveOrder(partner.getId());
        Long totalOrders = orderStatsQueryService.countByDeliveryPartner(partner.getId());

        return PartnerProfileResponseDto.builder()
                .partnerId(partner.getId())
                .name(user.getName())
                .vehicleType(partner.getVehicleType())
                .rating(partner.getRating())
                .city(partner.getCity())
                .joinedAt(partner.getCreatedAt())
                .totalDeliveries(totalOrders)
                .activeOrders(activeOrders)
                .availability(partner.getAvailability())
                .isActive(partner.getIsActive())
                .build();
    }

    @Override
    public List<PartnerOrderDetail> getPartnerCurrentOrder(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        DeliveryPartner partner = deliveryPartnerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_PARTNER));

        List<Order> orders = partnerQueryService.getActiveOrdersForPartner(partner.getId());

        return orders.stream().map(order ->
                PartnerOrderDetail.builder()
                        .orderId(order.getId())
                        .restaurantName(order.getRestaurant().getName())
                        .restaurantAddress(order.getRestaurant().getAddress())
                        .userName(order.getUser().getName())
                        .userAddress(null)
                        .orderStatus(order.getStatus().toString())
                        .totalItems(order.getTotalItems())
                        .orderedAt(order.getCreatedAt())
                        .build()).toList();

    }

    @Override
    public List<PartnerOrderDetail> getPartnerOrderHistory(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        DeliveryPartner partner = deliveryPartnerRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_PARTNER));

        List<Order> orders = partnerQueryService.getCompletedOrdersForPartner(partner.getId());

        return orders.stream().map(order ->
                PartnerOrderDetail.builder()
                        .orderId(order.getId())
                        .restaurantName(order.getRestaurant().getName())
                        .restaurantAddress(order.getRestaurant().getAddress())
                        .userName(order.getUser().getName())
                        .userAddress(null)
                        .orderStatus(order.getStatus().toString())
                        .totalItems(order.getTotalItems())
                        .orderedAt(order.getCreatedAt())
                        .build()).toList();

    }

    @Override
    public void updateStatus(Long userId, Long orderId, OrderStatus status) {
        DeliveryPartner partner = deliveryPartnerRepository.getByUserId(userId);

        orderCommandService.updateDeliveryStatus(
                orderId,
                partner.getId(),
                status
        );

        if(OrderStatus.DELIVERED.equals(status)){
            partner.setAvailability(DeliveryPartnerAvailability.ONLINE);
            deliveryPartnerRepository.save(partner);
        }
    }

}
