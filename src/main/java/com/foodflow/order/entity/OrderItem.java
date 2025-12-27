package com.foodflow.order.entity;

import com.foodflow.menu.entity.MenuItems;
import jakarta.persistence.*;
import lombok.Builder;

import java.math.BigDecimal;

@Entity
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Long menuItemId;
    private String name;

    private Integer quantity;

    private BigDecimal price;
    private BigDecimal totalAmount;
}
