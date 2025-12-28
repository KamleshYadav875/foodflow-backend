package com.foodflow.delivery.repository;

import com.foodflow.delivery.entity.DeliveryPartner;
import com.foodflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {
    boolean existsByUser(User user);

    List<DeliveryPartner> findOnlineByCity(String city);

   Optional<DeliveryPartner> findByUser(User user);

    DeliveryPartner getByUserId(Long userId);
}
