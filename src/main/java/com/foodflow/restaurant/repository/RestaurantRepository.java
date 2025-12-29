package com.foodflow.restaurant.repository;

import com.foodflow.restaurant.entity.Restaurant;
import com.foodflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByCity(String city);
    @Query("""
            SELECT r FROM Restaurant r 
            JOIN FETCH r.owner""")
    List<Restaurant> findAllWithOwner();


    boolean existsByOwner(User owner);
}
