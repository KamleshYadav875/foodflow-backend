package com.foodflow.restaurant.controller;

import com.foodflow.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
@PreAuthorize("hasRole('RESTAURANT')")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PutMapping("/{id}")
    public ResponseEntity<Void> getChangeRestaurantState(@PathVariable Long id){
        restaurantService.changeRestaurantState(id);
        return  ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{restaurantId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateMenuItemImage(@PathVariable Long restaurantId, @RequestPart(value = "image", required = false) MultipartFile image){
        restaurantService.updateResturantImage(restaurantId, image);
        return ResponseEntity.noContent().build();
    }

}
