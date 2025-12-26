package com.foodflow.restaurant.entity;

import com.foodflow.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(
                        name = "idx_restaurant_city",
                        columnList = "city"),
                @Index(
                        name = "idx_restaurant_city_open",
                        columnList = "city, is_open"
                ),
                @Index(
                        name = "idx_restaurant_city_rating",
                        columnList = "city, rating"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    private Double latitude;
    private Double longitude;

    private Boolean isOpen = true;

    private Integer avgPrepTimeMinutes;

    private Float rating;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    private String imageUrl;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
