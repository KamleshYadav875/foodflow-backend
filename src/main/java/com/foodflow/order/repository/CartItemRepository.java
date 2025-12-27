package com.foodflow.order.repository;

import com.foodflow.menu.entity.MenuItems;
import com.foodflow.order.entity.Cart;
import com.foodflow.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart savedCart);

    Optional<CartItem> findByCartAndMenuItems(Cart cart, MenuItems menuItem);

    void deleteByCart(Cart cart);
}
