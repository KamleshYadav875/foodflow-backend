package com.foodflow.order.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.common.util.Constant;
import com.foodflow.delivery.strategy.impl.CityBasedBroadcastStrategy;
import com.foodflow.order.dto.OrderDetailResponse;
import com.foodflow.order.dto.OrderItemResponse;
import com.foodflow.order.dto.OrderResponseDto;
import com.foodflow.order.dto.PageResponse;
import com.foodflow.order.entity.Cart;
import com.foodflow.order.entity.CartItem;
import com.foodflow.order.entity.Order;
import com.foodflow.order.entity.OrderItem;
import com.foodflow.order.enums.OrderStatus;
import com.foodflow.order.repository.CartItemRepository;
import com.foodflow.order.repository.CartRepository;
import com.foodflow.order.repository.OrderItemRepository;
import com.foodflow.order.repository.OrderRepository;
import com.foodflow.order.service.OrderService;
import com.foodflow.order.util.OrderStatusValidator;
import com.foodflow.payment.dto.PaymentLinkResponseDto;
import com.foodflow.payment.service.PaymentService;
import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.service.RestaurantQueryService;
import com.foodflow.user.entity.User;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final UserQueryService userQueryService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final RestaurantQueryService restaurantQueryService;
    private final CityBasedBroadcastStrategy cityBasedBroadcastStrategy;
    private final PaymentService paymentService;

    @Override
    @Transactional
    public OrderResponseDto checkout(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUser(user);

        if(cart == null){
            throw new BadRequestException("Cart is empty");
        }

        List<CartItem> cartItem = cartItemRepository.findByCart(cart);
        if(cartItem.isEmpty()){
            throw new BadRequestException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .restaurant(cart.getRestaurant())
                .totalItems(cart.getTotalItems())
                .totalAmount(cart.getTotalAmount())
                .status(OrderStatus.CREATED)
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = cartItem.stream()
                .map(item ->
                    OrderItem.builder()
                        .order(savedOrder)
                        .menuItemId(item.getMenuItems().getId())
                        .name(item.getMenuItems().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .totalAmount(item.getTotalPrice()).build()
                ).toList();

        orderItemRepository.saveAll(orderItems);

        cartItemRepository.deleteByCart(cart);
        cartRepository.delete(cart);

        PaymentLinkResponseDto paymentLink = paymentService.createPaymentLink(order.getId());

        return OrderResponseDto.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .totalItems(savedOrder.getTotalItems())
                .createdAt(savedOrder.getCreatedAt())
                .paymentId(paymentLink.getPaymentId())
                .paymentLink(paymentLink.getPaymentUrl())
                .build();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

        OrderStatus currentStatus = order.getStatus();

        OrderStatusValidator.validateTransition(currentStatus, status);

        if(status == OrderStatus.CANCELLED || status == OrderStatus.REJECTED)
            order.setCancelledAt(LocalDateTime.now());

        order.setStatus(status);
        Order update = orderRepository.save(order);

        if(update.getStatus().equals(OrderStatus.READY)){
            cityBasedBroadcastStrategy.broadcast(update);
        }

        return OrderResponseDto.builder()
                .orderId(update.getId())
                .status(update.getStatus())
                .totalAmount(update.getTotalAmount())
                .totalItems(update.getTotalItems())
                .createdAt(update.getCreatedAt())
                .build();
    }

    @Override
    public PageResponse<OrderResponseDto> getOrdersByUser(Long userId, int page, int size) {

        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Order> ordersPage =
                orderRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        List<OrderResponseDto> content =
                ordersPage.getContent()
                        .stream()
                        .map(order -> OrderResponseDto.builder()
                                .orderId(order.getId())
                                .status(order.getStatus())
                                .totalItems(order.getTotalItems())
                                .totalAmount(order.getTotalAmount())
                                .createdAt(order.getCreatedAt())
                                .build()
                        )
                        .toList();

        return PageResponse.<OrderResponseDto>builder()
                .content(content)
                .page(ordersPage.getNumber())
                .size(ordersPage.getSize())
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .last(ordersPage.isLast())
                .build();
    }

    @Override
    public PageResponse<OrderResponseDto> getOrderByRestaurant(Long restaurantId, int page, int size) {
        Restaurant restaurant = restaurantQueryService.getRestaurantById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException(Constant.RESTAURANT_NOT_FOUND));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Order> orderPage = orderRepository.findByRestaurantOrderByCreatedAtDesc(restaurant, pageable);

        List<OrderResponseDto> response = orderPage.getContent().stream()
                .map(order -> OrderResponseDto.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .totalItems(order.getTotalItems())
                        .createdAt(order.getCreatedAt())
                        .status(order.getStatus())
                        .build()).toList();

        return PageResponse.<OrderResponseDto>builder()
                .content(response)
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .last(orderPage.isLast())
                .totalPages(orderPage.getTotalPages())
                .build();

    }

    @Override
    public OrderDetailResponse getUserOrderDetails(Long userId, Long orderId) {
       User user = userQueryService.getUserById(userId)
               .orElseThrow(() -> new ResourceNotFoundException(Constant.USER_NOT_FOUND));

       Order order = orderRepository.findById(orderId)
               .orElseThrow(() -> new ResourceNotFoundException(Constant.ORDER_NOT_FOUND));

       if (!order.getUser().getId().equals(user.getId())) {
           throw new BadRequestException("You are not allowed to view this order");
       }

       List<OrderItem> orderItem = orderItemRepository.findByOrder(order);

       List<OrderItemResponse> itemResponseList = orderItem.stream().map(item -> OrderItemResponse.builder()
               .name(item.getName())
               .quantity(item.getQuantity())
               .price(item.getPrice())
               .totalAmount(item.getTotalAmount())
               .build()).toList();

       return OrderDetailResponse.builder()
               .orderId(order.getId())
               .status(order.getStatus())
               .orderedAt(order.getCreatedAt())
               .restaurantName(order.getRestaurant().getName())
               .partnerId(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getId())
               .partnerName(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getUser().getName())
               .partnerRating(order.getDeliveryPartner() == null ? null : order.getDeliveryPartner().getRating())
               .items(itemResponseList)
               .build();
    }
}
