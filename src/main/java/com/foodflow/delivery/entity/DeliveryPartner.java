package com.foodflow.delivery.entity;

import com.foodflow.delivery.enums.DeliveryPartnerAvailability;
import com.foodflow.delivery.enums.VehicleType;
import com.foodflow.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(indexes = {
        @Index(name = "idx_deliveryPartner_availability", columnList = "availability")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String city;

    @Enumerated(EnumType.STRING)
    private DeliveryPartnerAvailability availability = DeliveryPartnerAvailability.OFFLINE;

    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;
    private Integer totalDeliveries;
    private Double rating = 0.0;
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
