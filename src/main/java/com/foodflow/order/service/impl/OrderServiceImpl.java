package com.foodflow.order.service.impl;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.common.exceptions.ResourceNotFoundException;
import com.foodflow.order.dto.OrderResponseDto;
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
import com.foodflow.user.entity.User;
import com.foodflow.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserQueryService userQueryService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    @Transactional
    public OrderResponseDto checkout(Long userId) {
        User user = userQueryService.getUserById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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

        return OrderResponseDto.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .totalItems(savedOrder.getTotalItems())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();

        OrderStatusValidator.validateTransition(currentStatus, status);

        if(status == OrderStatus.CANCELLED || status == OrderStatus.REJECTED)
            order.setCancelledAt(LocalDateTime.now());

        order.setStatus(status);
        Order update = orderRepository.save(order);

        return OrderResponseDto.builder()
                .orderId(update.getId())
                .status(update.getStatus())
                .totalAmount(update.getTotalAmount())
                .totalItems(update.getTotalItems())
                .createdAt(update.getCreatedAt())
                .build();
    }
}
