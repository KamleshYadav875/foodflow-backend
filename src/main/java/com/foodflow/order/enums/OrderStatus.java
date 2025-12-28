package com.foodflow.order.enums;

public enum OrderStatus {

    CREATED,
    PLACED,           // Order placed, not yet accepted
    ACCEPTED,         // Restaurant accepted
    PREPARING,        // Food is being prepared
    READY,            // Ready for pickup
    OUT_FOR_PICKUP,   // Out for pick the order
    PICKED_UP,        // Picked by delivery partner
    DELIVERED,        // Successfully delivered
    CANCELLED,        // Cancelled by user/restaurant
    REJECTED          // Rejected by restaurant
}
