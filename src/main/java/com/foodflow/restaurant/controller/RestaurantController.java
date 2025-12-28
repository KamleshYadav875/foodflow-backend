package com.foodflow.restaurant.controller;

import com.foodflow.restaurant.dto.RestaurantDetailResponseDto;
import com.foodflow.restaurant.dto.RestaurantListResponseDto;
import com.foodflow.restaurant.dto.RestaurantRequestDto;
import com.foodflow.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // Create restaurant
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestaurantDetailResponseDto> createRestaurant(@RequestPart("restaurant") RestaurantRequestDto request, @RequestPart(value = "image", required = false) MultipartFile image){
        RestaurantDetailResponseDto response = restaurantService.createRestaurant(request, image);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RestaurantListResponseDto>> getAllRestaurants(){
        List<RestaurantListResponseDto> response = restaurantService.getAllRestaurants();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<RestaurantListResponseDto>> getAllRestaurantsByCity(@PathVariable String city){
        List<RestaurantListResponseDto> response = restaurantService.getAllRestaurantsByCity(city);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDetailResponseDto> getRestaurantById(@PathVariable Long id){
        RestaurantDetailResponseDto response = restaurantService.getRestaurantById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
