package com.foodflow.user.service.impl;

import com.foodflow.user.dto.OnboardRestaurantAdminRequest;
import com.foodflow.user.entity.User;
import com.foodflow.user.enums.UserRole;
import com.foodflow.user.enums.UserStatus;
import com.foodflow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User onboardRestaurantAdmin(String phone, String name){

        User user = userRepository.findByPhone(phone).orElse(null);

        if(!ObjectUtils.isEmpty(user)){
            if(user.getRoles().contains(UserRole.RESTAURANT))
                return user;

            user.getRoles().add(UserRole.RESTAURANT);
            user = userRepository.save(user);
            return user;
        }

        user = User.builder()
                .name(name)
                .phone(phone)
                .password(passwordEncoder.encode("Admin@123"))
                .roles(Set.of(UserRole.USER, UserRole.RESTAURANT))
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);

    }
}
