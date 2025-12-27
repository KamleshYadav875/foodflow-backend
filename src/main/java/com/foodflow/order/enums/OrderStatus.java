package com.foodflow.order.enums;

public enum OrderStatus {
    CREATED,          // Order placed, not yet accepted
    ACCEPTED,         // Restaurant accepted
    PREPARING,        // Food is being prepared
    READY,            // Ready for pickup
    PICKED_UP,        // Picked by delivery partner
    DELIVERED,        // Successfully delivered

    CANCELLED,        // Cancelled by user/restaurant
    REJECTED          // Rejected by restaurant
}
