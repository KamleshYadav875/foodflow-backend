package com.foodflow.restaurant.service;

import com.foodflow.restaurant.dto.RestaurantDetailResponseDto;
import com.foodflow.restaurant.dto.RestaurantListResponseDto;
import com.foodflow.restaurant.dto.RestaurantRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RestaurantService {

    RestaurantDetailResponseDto createRestaurant(RestaurantRequestDto request, MultipartFile image);

    List<RestaurantListResponseDto> getAllRestaurants();

    List<RestaurantListResponseDto> getAllRestaurantsByCity(String city);

    RestaurantDetailResponseDto getRestaurantById(Long id);

    void deleteRestaurant(Long id);


}
