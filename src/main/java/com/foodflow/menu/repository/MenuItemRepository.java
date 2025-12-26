package com.foodflow.menu.repository;

import com.foodflow.menu.entity.MenuItems;
import com.foodflow.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItems, Long> {


    List<MenuItems> findByRestaurant(Restaurant restaurant);
}
