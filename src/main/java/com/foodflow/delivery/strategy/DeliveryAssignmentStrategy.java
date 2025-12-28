package com.foodflow.delivery.strategy;

import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.order.entity.Order;

import java.util.List;

public interface DeliveryAssignmentStrategy  {

    void broadcast(Order order);
}
