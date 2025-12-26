package com.foodflow.restaurant.service.impl;

import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.restaurant.repository.RestaurantRepository;
import com.foodflow.restaurant.service.RestaurantQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RestaurantQueryServiceImpl implements RestaurantQueryService {

    private final RestaurantRepository restaurantRepository;
    @Override
    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
}
