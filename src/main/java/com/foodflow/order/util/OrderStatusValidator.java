package com.foodflow.order.util;

import com.foodflow.common.exceptions.BadRequestException;
import com.foodflow.order.enums.OrderStatus;

import java.util.Map;
import java.util.Set;

public class OrderStatusValidator {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS =
                Map.of(
                        OrderStatus.CREATED, Set.of(
                                OrderStatus.CANCELLED,
                                OrderStatus.PLACED

                        ),
                        OrderStatus.PLACED, Set.of(OrderStatus.ACCEPTED),
                        OrderStatus.ACCEPTED, Set.of(OrderStatus.PREPARING),
                        OrderStatus.PREPARING, Set.of(OrderStatus.READY),
                        OrderStatus.READY, Set.of(OrderStatus.OUT_FOR_PICKUP),
                        OrderStatus.OUT_FOR_PICKUP, Set.of(OrderStatus.PICKED_UP),
                        OrderStatus.PICKED_UP, Set.of(OrderStatus.DELIVERED)
                );

    public static void validateTransition(OrderStatus current, OrderStatus next){

        Set<OrderStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());

        // Terminal states
        if (current == OrderStatus.DELIVERED ||
                current == OrderStatus.CANCELLED ||
                current == OrderStatus.REJECTED) {
            throw new BadRequestException(
                    "Order already completed or cancelled"
            );
        }

        if(!allowed.contains(next)){
            throw new BadRequestException("Invalid order status transition from "+current +" to "+next);
        }
    }
}
