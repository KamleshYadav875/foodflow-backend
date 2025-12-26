package com.foodflow.restaurant.service;

import com.foodflow.restaurant.entity.Restaurant;

import java.util.Optional;

public interface RestaurantQueryService {

    Optional<Restaurant> getRestaurantById(Long id);
}
